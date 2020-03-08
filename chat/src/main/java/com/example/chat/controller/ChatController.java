package com.example.chat.controller;

import com.example.api.inner.inner.ConnectorService;
import com.example.proto.inner.inner.Inner;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kuro
 * @version V1.0
 * @date 3/6/20 1:07 PM
 **/
@Slf4j
@RestController
@RequestMapping("/")
public class ChatController {

    @Reference(version = "1.0.0")
    private ConnectorService connectorService;

    @GetMapping("/get")
    public String getHostInfo() {
        System.out.println("start");
        Inner.GetNodeAddresssReq req = Inner.GetNodeAddresssReq.newBuilder().build();
        Inner.GetNodeAddresssResp nodeAddress =
                connectorService.getNodeAddress(req);
        log.info("NodeAddress:{}", nodeAddress);
        return nodeAddress.toString();
    }

}