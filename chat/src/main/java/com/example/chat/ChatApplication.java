package com.example.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@SpringBootApplication
public class ChatApplication {

    @PostConstruct
    private void onStart() {

    }

    @PreDestroy
    private void destroy() {
        System.out.println("close");
    }

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

}
