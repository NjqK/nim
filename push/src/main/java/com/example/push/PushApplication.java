package com.example.push;

import com.example.common.kafka.KafkaProducerUtil;
import com.example.common.redis.JedisUtil;
import lombok.extern.slf4j.Slf4j;
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

    @PostConstruct
    private void onStart() {
        log.info(applicationName + " starting...");
        initRedis();
        initKafka();
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
        // TODO 添加dubbo优雅停机
        System.out.println("close");
    }

    public static void main(String[] args) {
        SpringApplication.run(PushApplication.class, args);
    }
}
