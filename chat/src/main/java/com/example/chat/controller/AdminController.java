package com.example.chat.controller;

import com.alibaba.fastjson.JSON;
import com.example.api.outer.outer.ChatService;
import com.example.chat.common.ServerTypeEnum;
import com.example.chat.entity.vo.RecoverServiceReqVO;
import com.example.chat.entity.vo.ReleaseConnectionsReqVO;
import com.example.common.CommonConstants;
import com.example.common.ServiceStatusEnum;
import com.example.common.redis.JedisUtil;
import com.example.common.util.ListUtil;
import com.example.common.zk.ZkUtil;
import com.example.proto.outer.outer.Outer;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kuro
 * @version V1.0
 * @date 3/6/20 1:07 PM
 **/
@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/releaseConnections")
    public String releaseConnections(@RequestBody ReleaseConnectionsReqVO releaseConnectionsReqVO) throws InvalidProtocolBufferException {
        Outer.ReleaseConnectionsReq req = Outer.ReleaseConnectionsReq.newBuilder()
                .setApplicationName(releaseConnectionsReqVO.getAppName())
                .setIp(releaseConnectionsReqVO.getIp())
                .setPort(releaseConnectionsReqVO.getPort())
                .build();
        Outer.ReleaseConnectionsResp resp = chatService.releaseConnections(req);
        return JsonFormat.printer().print(resp);
    }

    @PostMapping("/recoverServer")
    public String recoverService(@RequestBody RecoverServiceReqVO recoverServiceReqVO) throws InvalidProtocolBufferException {
        Outer.RecoverServiceReq req = Outer.RecoverServiceReq.newBuilder()
                .setApplicationName(recoverServiceReqVO.getAppName())
                .setIp(recoverServiceReqVO.getIp())
                .setPort(recoverServiceReqVO.getPort())
                .build();
        Outer.RecoverServiceResp resp = chatService.recoverService(req);
        return JsonFormat.printer().print(resp);
    }

    @GetMapping("/getServerInfo")
    public String getServerInfo(@RequestParam("serverType") String serverType)  {
        ServerTypeEnum parse = ServerTypeEnum.parse(serverType);
        if (parse == null) {
            return JSON.toJSONString(Collections.EMPTY_MAP);
        }
        Map<String, Map<String, String>> result = new HashMap<>(4);
        String regex = "_";
        switch (parse) {
            case CHAT:
                if (ZkUtil.isExists(CommonConstants.CHAT_ZK_BASE_PATH)) {
                    List<String> chatChild = ZkUtil.getChild(CommonConstants.CHAT_ZK_BASE_PATH);
                    if (ListUtil.isEmpty(chatChild)) {
                        return JSON.toJSONString(result);
                    }
                    for (String s : chatChild) {
                        Map<String, String> info = new HashMap<>(4);
                        String[] split = s.split(regex);
                        info.put("name", split[0]);
                        info.put("ip", split[1]);
                        result.put(s, info);
                    }
                }
                break;
            case PUSH:
                if (ZkUtil.isExists(CommonConstants.PUSH_ZK_BASE_PATH)) {
                    List<String> pushChild = ZkUtil.getChild(CommonConstants.PUSH_ZK_BASE_PATH);
                    if (ListUtil.isEmpty(pushChild)) {
                        return JSON.toJSONString(result);
                    }
                    for (String s : pushChild) {
                        Map<String, String> info = new HashMap<>(4);
                        String[] split = s.split(regex);
                        info.put("name", split[0]);
                        info.put("ip", split[1]);
                        result.put(s, info);
                    }
                }
                break;
            case CONNECTOR:
                if (ZkUtil.isExists(CommonConstants.CONNECTOR_ZK_BASE_PATH)) {
                    List<String> child = ZkUtil.getChild(CommonConstants.CONNECTOR_ZK_BASE_PATH);
                    if (ListUtil.isEmpty(child)) {
                        return JSON.toJSONString(result);
                    }
                    for (String key : child) {
                        Map<String, String> serverInfo = JedisUtil.hgetall(key);
//            String status = result.get("status");
//            if (status != null) {
//                ServiceStatusEnum statusEnum = ServiceStatusEnum.valueOf(Integer.parseInt(status));
//                result.put("status", statusEnum.getDesc());
//            }
                        result.put(key, serverInfo);
                    }
                }
        }
        return JSON.toJSONString(result);
    }

}