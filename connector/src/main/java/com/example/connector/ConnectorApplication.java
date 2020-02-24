package com.example.connector;

import com.example.connector.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;

@Slf4j
//@MapperScan("com.example.connector.dao.mybatis")
@SpringBootApplication
public class ConnectorApplication {

    @Resource
    private DemoService demoServiceImpl;

    @PostConstruct
    private void onStart() {
        log.info("Connector App starting on {}.", new Date().toString());
        demoServiceImpl.talk();
    }

    public static void main(String[] args) {
        SpringApplication.run(ConnectorApplication.class, args);
    }

}
