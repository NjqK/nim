package com.example.chat.manager.impl;

import com.example.api.inner.inner.PushService;
import com.example.chat.dao.manager.MsgInfoDaoManager;
import com.example.chat.dao.manager.MsgReadDaoManager;
import com.example.chat.entity.domain.MsgInfo;
import com.example.chat.entity.dto.MsgInfoDto;
import com.example.chat.manager.ChatServiceManager;
import com.example.common.CommonConstants;
import com.example.common.guid.UuidGenUtil;
import com.example.common.util.ListUtil;
import com.example.proto.common.common.Common;
import com.example.proto.inner.inner.Inner;
import com.example.proto.outer.outer.Outer;
import com.google.protobuf.InvalidProtocolBufferException;
import io.protostuff.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private MsgReadDaoManager msgReadDaoManager;

    @Reference(version = "1.0.0", async = true)
    private PushService pushService;

    @Override
    public Outer.DoGroupSendingResp doGroupSending(Outer.DoGroupSendingReq req) {

        return null;
    }

    @Override
    public Outer.SendMsgIndividuallyResp sendMsgIndividually(Outer.SendMsgIndividuallyReq req) {
        // 存到消息表然后发给push
        MsgInfoDto msgInfoDto = new MsgInfoDto();
        msgInfoDto.setFromUid(Long.valueOf(req.getFromUid()));
        msgInfoDto.setToUid(Long.valueOf(req.getToUid()));
        msgInfoDto.setMsgType(req.getMsgTypeValue());
        msgInfoDto.setMsgContentType(req.getMsgContentTypeValue());
        long uuid = UuidGenUtil.getUUID();
        msgInfoDto.setGuid(uuid);
        // 封装消息的类
        msgInfoDto.setMsgBody(Common.Body.newBuilder()
                .setContent(req.getMsgContent())
                .build());
        if (msgInfoDaoManager.addMsgInfo(msgInfoDto) != 1) {
            // return fail
            return Outer.SendMsgIndividuallyResp.newBuilder().setRet(CommonConstants.FAIL).build();
        }
        // invoke push
        Inner.RouteMsgReq routeMsgReq = buildPushReq(req, uuid);
        // Asynchronously invoke the push service
        log.info("route req:{}", req);
        pushService.routeMsg(routeMsgReq);
        return Outer.SendMsgIndividuallyResp.newBuilder().setRet(CommonConstants.SUCCESS).build();
    }

    @Override
    public Outer.GetUnreadMsgResp getUnreadMsg(Outer.GetUnreadMsgReq req) throws InvalidProtocolBufferException {
        long uid = Long.parseLong(req.getUid());
        long maxGuid = Long.parseLong(req.getMaxGuid());
        long receivedMaxGuid = msgReadDaoManager.getMaxGuid(uid);
        maxGuid = Long.max(maxGuid, receivedMaxGuid);
        List<MsgInfo> unreadMsg = msgInfoDaoManager.getUnreadMsg(uid, maxGuid);
        Outer.GetUnreadMsgResp.Builder builder = Outer.GetUnreadMsgResp.newBuilder();
        Common.ErrorMsg errMsg = Common.ErrorMsg.newBuilder()
                .setErrorCode(Common.ErrCode.SUCCESS)
                .build();
        builder.setRet(errMsg);
        if (!ListUtil.isEmpty(unreadMsg)) {
            List<Common.Msg> unread = new ArrayList<>(unreadMsg.size());
            Common.Msg.Builder msgBuilder = Common.Msg.newBuilder();
            Common.Head.Builder headBuilder = Common.Head.newBuilder();
            for (MsgInfo msgInfo : unreadMsg) {
                Common.Head head = headBuilder.setFromId(msgInfo.getFromUid())
                        .setToId(uid)
                        .setMsgTypeValue(msgInfo.getMsgType())
                        .setMsgContentTypeValue(msgInfo.getMsgContentType())
                        .setMsgId(msgInfo.getGuid())
                        .build();
                Common.Body body = Common.Body.parseFrom(msgInfo.getMsgData());
                Common.Msg msg = msgBuilder
                        .setHead(head)
                        .setBody(body)
                        .build();
                builder.addMsgs(msg);
                msgBuilder.clear();
                headBuilder.clear();
            }
        }
        return builder.build();
    }

    @Override
    public Outer.AckMsgResp ackMsg(Outer.AckMsgReq ackMsgReq) {
        long uid = Long.parseLong(ackMsgReq.getUid());
        long guid = Long.parseLong(ackMsgReq.getGuid());
        long maxGuid = msgReadDaoManager.getMaxGuid(uid);
        if (maxGuid == 0L) {
            // 还没有，直接insert
            if (msgReadDaoManager.insertMaxGuid(uid, guid) != 1L) {
                Common.ErrorMsg errorMsg = Common.ErrorMsg.newBuilder()
                        .setErrorCode(Common.ErrCode.ACK_MSG_RESP_UPDATE_DB_FAIL)
                        .setMsg("insert ackMsg数据库失败")
                        .build();
                return Outer.AckMsgResp.newBuilder().setRet(errorMsg).build();
            }
        } else if (maxGuid != guid){
            if (msgReadDaoManager.updateMaxGuid(uid, guid) != 1L) {
                // return value != 1 means updating database is failed
                Common.ErrorMsg errorMsg = Common.ErrorMsg.newBuilder()
                        .setErrorCode(Common.ErrCode.ACK_MSG_RESP_UPDATE_DB_FAIL)
                        .setMsg("update ackMsg数据库失败")
                        .build();
                return Outer.AckMsgResp.newBuilder().setRet(errorMsg).build();
            }
        }
        return Outer.AckMsgResp.newBuilder().setRet(CommonConstants.SUCCESS).build();
    }

    private Inner.RouteMsgReq buildPushReq(Outer.SendMsgIndividuallyReq req, long guid) {
        Common.Head header = Common.Head.newBuilder()
                .setMsgId(guid)
                .setToId(Long.parseLong(req.getToUid()))
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
}
