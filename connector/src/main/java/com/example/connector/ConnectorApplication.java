package com.example.connector;

import com.example.common.CommonConstants;
import com.example.common.util.JedisUtil;
import com.example.connector.common.RedisKeyUtil;
import com.example.connector.dao.manager.ClusterNodeManager;
import com.example.connector.entity.cluster.ClusterNode;
import com.example.connector.netty.NettyServerManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.DubboShutdownHook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

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

    private String zkRootPath = "/app/";

    @Autowired
    private ClusterNodeManager clusterNodeManager;

    @PostConstruct
    private void onStart() {
        log.info(applicationName + " starting...");
        initRedis();
        initNetty();
        // 用这个clusterNode在redis李建立一个map，并创建ServerBootstrap
    }

    private void initNetty() {
        // 作为netty的host和port
        ClusterNode localNode = clusterNodeManager.getLocalNode();
        log.info("get local node for netty:{}", localNode);
        // init netty
        NettyServerManager instance = NettyServerManager.getInstance();
        instance.init(localNode);
        // create redis map of this netty node
        String key = RedisKeyUtil.createApplicationRedisKey(applicationName, localNode);
        String kafkaTopic = localNode.toString();
        JedisUtil.hsetnx(CommonConstants.CONNECTOR_REDIS_KEY, key, kafkaTopic);
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
        // TODO 删掉zk节点，释放netty资源，dubbo等
        JedisUtil.hdel(CommonConstants.CONNECTOR_REDIS_KEY, RedisKeyUtil.getApplicationRedisKey());
        JedisUtil.close();
        // TODO 添加dubbo优雅停机
        System.out.println("close");
    }

    public static void main(String[] args) {
        SpringApplication.run(ConnectorApplication.class, args);
    }

}
