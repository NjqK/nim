package com.example.chat.controller;

import com.example.api.outer.outer.ChatService;
import com.example.proto.outer.outer.Outer;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/getAvailableNode")
    public byte[] getHostInfo() throws InvalidProtocolBufferException {
        Outer.GetAvailableNodeResp resp = chatService.getAvailableNode(Outer.GetAvailableNodeReq.newBuilder().build());
        return resp.toByteArray();
    }

    @GetMapping("/getMsg")
    public byte[] getUnreadMsg(@RequestParam("uid") String uid, @RequestParam("mGuid") String maxGuid) throws InvalidProtocolBufferException {
        Outer.GetUnreadMsgReq req = Outer.GetUnreadMsgReq.newBuilder().setUid(uid).setMaxGuid(maxGuid).build();
        Outer.GetUnreadMsgResp resp = chatService.getUnreadMsg(req);
        return resp.toByteArray();
    }

    @GetMapping("/ackMsg")
    public byte[] ackMsg(@RequestParam("uid") String uid, @RequestParam("guid") String guid) {
        Outer.AckMsgReq req = Outer.AckMsgReq.newBuilder()
                .setUid(uid)
                .setGuid(guid)
                .build();
        Outer.AckMsgResp resp = chatService.ackMsg(req);
        return resp.toByteArray();
    }

}