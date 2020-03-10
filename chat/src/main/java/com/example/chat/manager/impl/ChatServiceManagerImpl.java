package com.example.chat.manager.impl;

import com.example.api.inner.inner.PushService;
import com.example.chat.dao.manager.MsgInfoDaoManager;
import com.example.chat.entity.dto.MsgInfoDto;
import com.example.chat.manager.ChatServiceManager;
import com.example.common.CommonConstants;
import com.example.common.guid.UuidGenUtil;
import com.example.proto.common.common.Common;
import com.example.proto.inner.inner.Inner;
import com.example.proto.outer.outer.Outer;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-9 下午1:19
 **/
@Slf4j
@Component
public class ChatServiceManagerImpl implements ChatServiceManager {

    @Autowired
    private MsgInfoDaoManager msgInfoDaoManager;

    @Reference(version = "1.0.0")
    private PushService pushService;

    @Override
    public Outer.DoGroupSendingResp doGroupSending(Outer.DoGroupSendingReq req) {
        return null;
    }

    @Override
    public Outer.SendMsgIndividuallyResp sendMsgIndividually(Outer.SendMsgIndividuallyReq req) {
        // TODO 存到消息表然后发给push
        MsgInfoDto msgInfoDto = new MsgInfoDto();
        msgInfoDto.setFromUid(Long.valueOf(req.getFromUid()));
        msgInfoDto.setToUid(Long.valueOf(req.getToUid()));
        msgInfoDto.setMsgType(req.getMsgTypeValue());
        msgInfoDto.setMsgContentType(req.getMsgContentTypeValue());
        long uuid = UuidGenUtil.getUUID();
        msgInfoDto.setGuid(uuid);
        // TODO 封装消息的类
        msgInfoDto.setMsgBody(Common.Body.newBuilder()
                .setContent("ss")
                .build());
        if (msgInfoDaoManager.addMsgInfo(msgInfoDto) != 1) {
            // TODO return fail
            return Outer.SendMsgIndividuallyResp.newBuilder().setRet(CommonConstants.FAIL).build();
        }
        // TODO 转发给push
        Inner.RouteMsgReq routeMsgReq = buildPushReq(req, uuid);
        // TODO Asynchronously invoke the push service
        log.info("route req:{}", req);
        pushService.routeMsg(routeMsgReq);
        return Outer.SendMsgIndividuallyResp.newBuilder().setRet(CommonConstants.SUCCESS).build();
    }

    private Inner.RouteMsgReq buildPushReq(Outer.SendMsgIndividuallyReq req, long guid) {
        Common.Head header = Common.Head.newBuilder()
                .setMsgId(guid)
                .setMsgType(req.getMsgType())
                .setMsgContentType(req.getMsgContentType())
                .build();
        Common.Body body = Common.Body.newBuilder()
                .setContent(req.getMsgContent())
                .build();
        return Inner.RouteMsgReq.newBuilder()
                .setToUid(req.getToUid())
                .setMsg(Common.Msg.newBuilder()
                        .setHead(header)
                        .setBody(body)
                        .build())
                .build();
    }

//    public String send() {
//        Inner.RouteMsgReq req = Inner.RouteMsgReq.newBuilder()
//                .setToUid("1")
//                .build();
//        return pushService.routeMsg(req).toString();
//    }
}
