package com.example.chat.controller;

import com.example.api.inner.inner.ConnectorService;
import com.example.api.inner.inner.PushService;
import com.example.api.outer.outer.ChatService;
import com.example.chat.manager.ChatServiceManager;
import com.example.chat.manager.impl.ChatServiceManagerImpl;
import com.example.chat.service.impl.ChatServiceImpl;
import com.example.common.guid.UuidGenUtil;
import com.example.proto.common.common.Common;
import com.example.proto.inner.inner.Inner;
import com.example.proto.outer.outer.Outer;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Autowired
    private ChatService chatService;

//    @Reference(version = "1.0.0")
//    private PushService pushService;

//    @Autowired
//    private ChatServiceManagerImpl chatServiceManager;

    @GetMapping("/getAvailableNode")
    public String getHostInfo() {
        Inner.GetNodeAddresssReq req = Inner.GetNodeAddresssReq.newBuilder().build();
        Inner.GetNodeAddresssResp nodeAddress =
                connectorService.getNodeAddress(req);
        log.info("NodeAddress:{}", nodeAddress);
        return nodeAddress.toString();
    }

    @GetMapping("/sendMsg")
    public String sendMsg(@RequestParam("uid") String uid) {
        // TODO 转化对象
        Outer.SendMsgIndividuallyReq req = Outer.SendMsgIndividuallyReq.newBuilder()
                .setToUid(uid)
                .setFromUid("2")
                .setMsgType(Common.MsgType.SINGLE_CHAT)
                .setMsgContentType(Common.MsgContentType.TEXT)
                .setMsgContent("hello")
                .build();
        Outer.SendMsgIndividuallyResp resp = chatService.sendMsgIndividually(req);
        return resp.toString();
    }

    @GetMapping("/gen")
    public String gen() {
        return String.valueOf(UuidGenUtil.getUUID());
    }

//    @GetMapping("/send")
//    public String send() {
//        Inner.RouteMsgReq req = Inner.RouteMsgReq.newBuilder()
//                .setToUid("1")
//                .build();
//        return pushService.routeMsg(req).toString();
//    }
//
//    @GetMapping("/send1")
//    public String send1() {
//        return chatService.send();
//    }
//
//    @GetMapping("/send2")
//    public String send2() {
//        return chatServiceManager.send();
//    }


}