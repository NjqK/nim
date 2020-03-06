package com.example.connector;

import lombok.extern.slf4j.Slf4j;
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

    @Value("${redis.cluster.nodes}")
    private String redisNodes;

    @Value("${redis.cluster.password}")
    private String redisPassword;

    @Value("${spring.application.name}")
    private String applicationName;

    private String zkRootPath = "/app/";

    @PostConstruct
    private void onStart() {
        System.out.println("start");
    }

    @PreDestroy
    private void onDestroy() {
        // TODO 删掉zk节点，释放netty资源，dubbo等
        System.out.println("close");
    }

    public static void main(String[] args) {
        SpringApplication.run(ConnectorApplication.class, args);
    }

}
