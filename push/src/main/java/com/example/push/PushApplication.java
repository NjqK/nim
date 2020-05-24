package com.example.push;

import com.example.common.CommonConstants;
import com.example.common.kafka.KafkaProducerUtil;
import com.example.common.redis.JedisUtil;
import com.example.common.zk.ZkUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.NetUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZKUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@SpringBootApplication
public class PushApplication {

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

    @Value("${zookeeper.url}")
    private String zkUrl;

    private String serverInfo = "Push_" + NetUtils.getLocalHost();

    @PostConstruct
    private void onStart() {
        log.info(applicationName + " starting...");
        initRedis();
        initKafka();
        initZk();
        String zkPath = CommonConstants.PUSH_ZK_BASE_PATH + "/" + serverInfo;
        if (!ZkUtil.isExists(CommonConstants.BASE_ZK_PATH)) {
            ZkUtil.createPath(CommonConstants.BASE_ZK_PATH, "", CreateMode.PERSISTENT);
        }
        if (!ZkUtil.isExists(CommonConstants.PUSH_ZK_BASE_PATH)) {
            ZkUtil.createPath(CommonConstants.PUSH_ZK_BASE_PATH, "", CreateMode.PERSISTENT);
        }
        ZkUtil.createPath(zkPath, "", CreateMode.EPHEMERAL);
    }

    private void initZk() {
        ZkUtil.start(zkUrl, null);
    }

    private void initKafka() {
        KafkaProducerUtil.init(kafkaNodes, applicationName, null);
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
        JedisUtil.close();
        KafkaProducerUtil.close();
        ZkUtil.deletePath(CommonConstants.PUSH_ZK_BASE_PATH + "/" + serverInfo);
        ZkUtil.releaseConnection();
        // TODO 添加dubbo优雅停机
        System.out.println("close");
    }

    public static void main(String[] args) {
        SpringApplication.run(PushApplication.class, args);
    }
}
