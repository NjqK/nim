package com.example.netty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;

/**
 * @author kuro
 */
@SpringBootApplication
public class NettyApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettyApplication.class, args);
    }

    @PostConstruct
    public void onStart() {
        System.out.printf("[===Server start successfully!===%s]\n", new Date().toString());
    }

    @PreDestroy
    public void onDestroy() {
        System.out.printf("[===Server end successfully!===%s]\n", new Date().toString());
    }

}
