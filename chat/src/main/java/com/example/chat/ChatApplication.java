package com.example.chat;

import com.example.api.inner.inner.ClusterNodeAddressService;
import com.example.proto.inner.inner.Inner;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@SpringBootApplication
public class ChatApplication {

    @Reference(version = "1.0.0", check = false)
    private ClusterNodeAddressService clusterNodeAddressService;

    @PostConstruct
    private void onStart() {
        System.out.println("start");
        System.out.println(clusterNodeAddressService);
        Inner.GetNodeAddresssReq req = Inner.GetNodeAddresssReq.newBuilder().build();
        Inner.GetNodeAddresssResp nodeAddress =
                clusterNodeAddressService.getNodeAddress(req);
        System.out.println(nodeAddress);
    }

    @PreDestroy
    private void destroy() {
        System.out.println("close");
    }

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

}
