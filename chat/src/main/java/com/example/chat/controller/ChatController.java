package com.example.chat.controller;

import com.alibaba.fastjson.JSON;
import com.example.api.outer.outer.ChatService;
import com.example.chat.entity.vo.RecoverServiceReqVO;
import com.example.chat.entity.vo.ReleaseConnectionsReqVO;
import com.example.chat.entity.vo.SendGroupMsgReq;
import com.example.common.CommonConstants;
import com.example.common.ServiceStatusEnum;
import com.example.common.redis.JedisUtil;
import com.example.common.util.ListUtil;
import com.example.common.zk.ZkUtil;
import com.example.proto.common.common.Common;
import com.example.proto.outer.outer.Outer;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
@RequestMapping("/")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/getAvailableNode")
    public byte[] getHostInfo() throws InvalidProtocolBufferException {
        Outer.GetAvailableNodeResp resp = chatService.getAvailableNode(Outer.GetAvailableNodeReq.newBuilder().build());
        return resp.toByteArray();
    }

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

    @GetMapping("/getConnectorInfo")
    public String getConnectorInfo()  {
        List<String> child = ZkUtil.getChild(CommonConstants.CONNECTOR_ZK_BASE_PATH);
        if (ListUtil.isEmpty(child)) {
            return "没有可用的Connector服务";
        }
        Map<String, Map<String, String>> result = new HashMap<>(child.size());
        for (String key : child) {
            Map<String, String> serverInfo = JedisUtil.hgetall(key);
            String status = serverInfo.get("status");
            if (status != null) {
                ServiceStatusEnum statusEnum = ServiceStatusEnum.valueOf(Integer.parseInt(status));
                serverInfo.put("status", statusEnum.getDesc());
            }
            result.put(key, serverInfo);
        }
        return JSON.toJSONString(result, true);
    }


    @GetMapping("/sendMsg")
    public String sendMsg(@RequestParam("uid") String uid, @RequestParam("msg") String msg) {
        // TODO 转化对象
        Outer.SendMsgIndividuallyReq req = Outer.SendMsgIndividuallyReq.newBuilder()
                .setToUid(uid)
                .setFromUid("2")
                .setMsgType(Common.MsgType.SINGLE_CHAT)
                .setMsgContentType(Common.MsgContentType.TEXT)
                .setMsgContent(msg)
                .build();
        Outer.SendMsgIndividuallyResp resp = chatService.sendMsgIndividually(req);
        return resp.toString();
    }

    @PostMapping("/sendGroupMsg")
    public String sendMsg(@RequestBody SendGroupMsgReq req) throws InvalidProtocolBufferException {
        Outer.DoGroupSendingReq.Builder builder = Outer.DoGroupSendingReq.newBuilder();
        String toUids = req.getToUids();
        // TODO 当uidList太大需要分批次做，以免大块内存导致FULL GC或者OOM
        if (StringUtils.isNoneEmpty(toUids)) {
            String[] split = toUids.split(",");
            ArrayList<String> strings = new ArrayList<>(split.length);
            for (String s : split) {
                if (StringUtils.isNoneEmpty(s)) {
                    strings.add(s);
                }
            }
            builder.addAllToUids(strings);
        }
        builder.setFromUid(req.getFromUid());
        builder.setMsgType(Common.MsgType.MULTI_CHAT);
        builder.setMsgContentType(Common.MsgContentType.valueOf(req.getMsgContentType()));
        builder.setMsgContent(req.getMsgContent());
        Outer.DoGroupSendingResp resp = chatService.doGroupSending(builder.build());
        return JsonFormat.printer().print(resp);
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