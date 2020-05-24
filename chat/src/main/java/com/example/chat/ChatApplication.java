package com.example.chat;

import com.example.common.CommonConstants;
import com.example.common.redis.JedisUtil;
import com.example.common.zk.ZkUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.NetUtils;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@SpringBootApplication
public class ChatApplication {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${zookeeper.url}")
    private String zkUrl;

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

    private String serverInfo = "Chat_" + NetUtils.getLocalHost();


    @PostConstruct
    private void onStart() {
        initZk();
        initRedis();
        String zkPath = CommonConstants.CHAT_ZK_BASE_PATH + "/" + serverInfo;
        if (!ZkUtil.isExists(CommonConstants.BASE_ZK_PATH)) {
            ZkUtil.createPath(CommonConstants.BASE_ZK_PATH, "", CreateMode.PERSISTENT);
        }
        if (!ZkUtil.isExists(CommonConstants.CHAT_ZK_BASE_PATH)) {
            ZkUtil.createPath(CommonConstants.CHAT_ZK_BASE_PATH, "", CreateMode.PERSISTENT);
        }
        ZkUtil.createPath(zkPath, "", CreateMode.EPHEMERAL);
    }

    private void initRedis() {
        int port = Integer.parseInt(redisPort);
        int maxTotal = Integer.parseInt(redisMaxTotal);
        int maxIdle = Integer.parseInt(redisMaxIdle);
        int maxWait = Integer.parseInt(redisMaxWaitMillis);
        JedisUtil.initJedisUtil(redisHost, port, redisPassword, maxTotal, maxIdle, maxWait);
    }

    private void initZk() {
        ZkUtil.start(zkUrl, null);
    }

    @PreDestroy
    private void destroy() {
        ZkUtil.deletePath(CommonConstants.CHAT_ZK_BASE_PATH + "/" + serverInfo);
        ZkUtil.releaseConnection();
        JedisUtil.close();
        System.out.println("close");
    }

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

}
