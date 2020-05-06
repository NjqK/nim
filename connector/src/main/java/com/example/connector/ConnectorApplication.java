package com.example.connector;

import com.example.common.CommonConstants;
import com.example.common.ServiceStatusEnum;
import com.example.common.kafka.KafkaConsumerUtil;
import com.example.common.kafka.KafkaProducerUtil;
import com.example.common.redis.JedisUtil;
import com.example.common.zk.ZkUtil;
import com.example.connector.common.ConnectorThreadFactory;
import com.example.connector.common.DubboRouterUtil;
import com.example.connector.common.KeyManager;
import com.example.connector.common.RedisKeyUtil;
import com.example.connector.dao.manager.ClusterNodeManager;
import com.example.connector.dao.manager.SessionManager;
import com.example.connector.dao.manager.impl.ConnectorProcessor;
import com.example.connector.dao.manager.impl.WeightCalculator;
import com.example.connector.entity.domain.ClusterNode;
import com.example.connector.entity.domain.RatePolicy;
import com.example.connector.netty.NettyServerManager;
import com.example.connector.task.UpdateServerLoadTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootApplication
public class ConnectorApplication {

    @Value("${zookeeper.url}")
    private String zkUrl;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${redis.pool.host}")
    private String redisHost;

    @Value("${redis.pool.port}")
    private String redisPort;

    @Value("${redis.pool.password}")
    private String redisPassword;

    @Value("${redis.pool.maxTotal}")
    private String redisMaxTotal;

    @Value("${redis.pool.maxIdle}")
    private String redisMaxIdle;

    @Value("${redis.pool.maxWaitMillis}")
    private String redisMaxWaitMillis;

    @Value(("${kafka.nodes}"))
    private String kafkaNodes;

    @Value("${kafka.group}")
    private String kafkaGroup;

    @Autowired
    private ClusterNodeManager clusterNodeManager;

    @Autowired
    private SessionManager sessionManager;

    @PostConstruct
    private void onStart() {
        log.info(applicationName + " starting...");
        initZk();
        initRedis();
        initNetty();
        // 用这个clusterNode在redis李建立一个map，并创建ServerBootstrap
        initKafka();
        // 添加zk临时节点
        String zkPath = CommonConstants.CONNECTOR_ZK_BASE_PATH + "/" + RedisKeyUtil.getApplicationRedisKey();
        if (!ZkUtil.isExists(CommonConstants.BASE_ZK_PATH)) {
            ZkUtil.createPath(CommonConstants.BASE_ZK_PATH, "", CreateMode.PERSISTENT);
        }
        if (!ZkUtil.isExists(CommonConstants.CONNECTOR_ZK_BASE_PATH)) {
            ZkUtil.createPath(CommonConstants.CONNECTOR_ZK_BASE_PATH, "", CreateMode.PERSISTENT);
        }
        ZkUtil.createPath(zkPath, "", CreateMode.EPHEMERAL);
        // 添加这个节点到redis，分别为connector，服务器key，权重
        JedisUtil.hsetnx(CommonConstants.CONNECTOR_REDIS_KEY, RedisKeyUtil.getApplicationRedisKey(), "0");
        JedisUtil.hsetnx(RedisKeyUtil.getApplicationRedisKey(), "weight", "0");
        JedisUtil.hset(RedisKeyUtil.getApplicationRedisKey(), "userCount", "0");
        JedisUtil.hsetnx(RedisKeyUtil.getApplicationRedisKey(), "status", String.valueOf(ServiceStatusEnum.IN_SERVICE.getStatus()));
        DubboRouterUtil.init(zkUrl);
        // 添加定时更新负载的任务
        RatePolicy ratePolicy = RatePolicy.DEFAULT;
        WeightCalculator weightCalculator = new WeightCalculator(ratePolicy);
        ConnectorThreadFactory.addScheduledJob(new UpdateServerLoadTask(weightCalculator), 60, TimeUnit.SECONDS);
    }

    private void initZk() {
        ZkUtil.start(zkUrl, null);
    }

    private void initKafka() {
        ClusterNode localNode = clusterNodeManager.getLocalNode();
        List<String> kafkaTopics = new ArrayList<>();
        kafkaTopics.add(CommonConstants.CONNECTOR_KAFKA_TOPIC);
        KafkaProducerUtil.init(kafkaNodes, applicationName, null);
        String groupName = kafkaGroup + localNode.getIp() + "_" + localNode.getPort();
        KafkaConsumerUtil.init(kafkaNodes, groupName, kafkaTopics
                , new ConnectorProcessor(NettyServerManager.getInstance(), sessionManager), true);
    }

    private void initNetty() {
        // 作为netty的host和port
        ClusterNode localNode = clusterNodeManager.getLocalNode();
        log.info("get local node for netty:{}", localNode);
        // init netty
        initKeyManager();
        NettyServerManager instance = NettyServerManager.getInstance();
        instance.init(localNode);
        RedisKeyUtil.createApplicationRedisKey(applicationName, localNode);
    }

    private void initKeyManager() {
        ClassPathResource classPathResource = new ClassPathResource("secure/rsa/private_key.txt");
        try (InputStream inputStream = classPathResource.getInputStream();
             InputStreamReader bufferedInputStream = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(bufferedInputStream);) {
            StringBuilder sb = new StringBuilder();
            String s = "";
            while ((s = bufferedReader.readLine()) != null) {
                sb.append(s + "\n");
            }
            String privateKey = sb.toString();
            new KeyManager(privateKey);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("读取服务端密钥失败");
        }
    }

    private void initRedis() {
        int port = Integer.parseInt(redisPort);
        int maxTotal = Integer.parseInt(redisMaxTotal);
        int maxIdle = Integer.parseInt(redisMaxIdle);
        int maxWait = Integer.parseInt(redisMaxWaitMillis);
        JedisUtil.initJedisUtil(redisHost, port, redisPassword, maxTotal, maxIdle, maxWait);
    }

    @PreDestroy
    private void onDestroy() {
        JedisUtil.hdel(CommonConstants.CONNECTOR_REDIS_KEY, RedisKeyUtil.getApplicationRedisKey());
        JedisUtil.del(RedisKeyUtil.getApplicationRedisKey());
        ZkUtil.deletePath(CommonConstants.CONNECTOR_ZK_BASE_PATH + "/" + RedisKeyUtil.getApplicationRedisKey());
        sessionManager.serverDown();
        JedisUtil.close();
        ZkUtil.releaseConnection();
        KafkaProducerUtil.close();
        KafkaConsumerUtil.destory();
        // TODO 添加dubbo优雅停机
        System.out.println("close");
    }

    public static void main(String[] args) {
        SpringApplication.run(ConnectorApplication.class, args);
    }

}
