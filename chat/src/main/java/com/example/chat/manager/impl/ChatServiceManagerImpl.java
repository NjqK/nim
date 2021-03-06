package com.example.chat.manager.impl;

import com.example.api.inner.inner.PushService;
import com.example.chat.dao.manager.MsgInfoDaoManager;
import com.example.chat.dao.manager.MsgReadDaoManager;
import com.example.chat.entity.domain.MsgInfo;
import com.example.chat.entity.domain.MsgInfoMongo;
import com.example.chat.entity.dto.MsgInfoDto;
import com.example.chat.manager.ChatServiceManager;
import com.example.common.CommonConstants;
import com.example.common.guid.UuidGenUtil;
import com.example.common.util.ListUtil;
import com.example.proto.common.common.Common;
import com.example.proto.inner.inner.Inner;
import com.example.proto.outer.outer.Outer;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.ProtocolStringList;
import com.google.protobuf.util.JsonFormat;
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
        ProtocolStringList toUidsList = req.getToUidsList();
        int uidSize = toUidsList.size();
        log.info("ChatServiceManagerImpl doGroupSending send uidSize:{}", uidSize);
        long[] uuiDs = UuidGenUtil.getUUIDs(uidSize);
        int uuidIndex = 0;
        long fromUid = Long.parseLong(req.getFromUid());
        List<MsgInfoDto> msgInfoDtos = new ArrayList<>(uidSize);
        Common.Body body = Common.Body.newBuilder().setContent(req.getMsgContent()).build();
        for (String toUid : toUidsList) {
            MsgInfoDto msgInfoDto = new MsgInfoDto();
            msgInfoDto.setFromUid(fromUid);
            msgInfoDto.setToUid(Long.valueOf(toUid));
            msgInfoDto.setMsgType(req.getMsgTypeValue());
            msgInfoDto.setMsgContentType(req.getMsgContentTypeValue());
            msgInfoDto.setGuid(uuiDs[uuidIndex++]);
            msgInfoDto.setMsgBody(body);
            msgInfoDtos.add(msgInfoDto);
        }
        if (msgInfoDaoManager.addMsgInfos(msgInfoDtos) != uidSize) {
            log.error("doGroupSending batch insert fail");
        }
        // 发给push
        Inner.BatchRouteMsgReq.Builder batchRouteMsgReqBuilder = builderBatchRouteReq(req, fromUid, msgInfoDtos);
        pushService.batchRouteMsg(batchRouteMsgReqBuilder.build());
        return Outer.DoGroupSendingResp.newBuilder().setRet(CommonConstants.SUCCESS).build();
    }

    private Inner.BatchRouteMsgReq.Builder builderBatchRouteReq(Outer.DoGroupSendingReq req,
                                                                long fromUid, List<MsgInfoDto> msgInfoDtos) {
        Common.Head header = Common.Head.newBuilder()
                .setFromId(fromUid)
                .setMsgType(req.getMsgType())
                .setMsgContentType(req.getMsgContentType())
                .build();
        Common.Body body = Common.Body.newBuilder()
                .setContent(req.getMsgContent())
                .build();
        Common.Msg msg = Common.Msg.newBuilder()
                .setHead(header)
                .setBody(body)
                .build();
        Inner.GuidUidBinder.Builder binderBuilder = Inner.GuidUidBinder.newBuilder();
        Inner.BatchRouteMsgReq.Builder batchRouteMsgReqBuilder = Inner.BatchRouteMsgReq.newBuilder().setMsg(msg);
        for (MsgInfoDto msgInfoDto : msgInfoDtos) {
            binderBuilder.clear();
            binderBuilder.setGuid(msgInfoDto.getGuid());
            binderBuilder.setUid(msgInfoDto.getToUid());
            batchRouteMsgReqBuilder.addToUid(binderBuilder.build());
        }
        return batchRouteMsgReqBuilder;
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
        List<MsgInfoMongo> unreadMsg = msgInfoDaoManager.getUnreadMsg(uid, maxGuid);
        Outer.GetUnreadMsgResp.Builder builder = Outer.GetUnreadMsgResp.newBuilder();
        Common.ErrorMsg errMsg = Common.ErrorMsg.newBuilder()
                .setErrorCode(Common.ErrCode.SUCCESS)
                .build();
        builder.setRet(errMsg);
        if (!ListUtil.isEmpty(unreadMsg)) {
            Common.Msg.Builder msgBuilder = Common.Msg.newBuilder();
            Common.Head.Builder headBuilder = Common.Head.newBuilder();
            for (MsgInfoMongo msgInfo : unreadMsg) {
                maxGuid = Long.max(maxGuid, msgInfo.getGuid());
                Common.Head head = headBuilder.setFromId(msgInfo.getFromUid())
                        .setToId(uid)
                        .setMsgTypeValue(msgInfo.getMsgType())
                        .setMsgContentTypeValue(msgInfo.getMsgContentType())
                        .setMsgId(msgInfo.getGuid())
                        .build();
                Common.Body.Builder bodyBuilder = Common.Body.newBuilder();
                JsonFormat.parser().merge(msgInfo.getMsgData(), bodyBuilder);
                Common.Msg msg = msgBuilder
                        .setHead(head)
                        .setBody(bodyBuilder.build())
                        .build();
                builder.addMsgs(msg);
                msgBuilder.clear();
                headBuilder.clear();
            }
        }
        // 更新最大guid
        if (receivedMaxGuid == 0L) {
            msgReadDaoManager.insertMaxGuid(uid, maxGuid);
        } else {
            msgReadDaoManager.updateMaxGuid(uid, maxGuid);
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
        } else if (maxGuid != guid) {
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

    @Override
    public Outer.ReleaseConnectionsResp releaseConnections(Outer.ReleaseConnectionsReq req) {
        String cmd = req.getApplicationName() + "_" + req.getIp() + "_" + req.getPort();
        Common.Head header = Common.Head.newBuilder()
                .setMsgType(Common.MsgType.CHANGE_SERVER)
                .build();
        Common.Body body = Common.Body.newBuilder()
                .setContent(cmd)
                .build();
        Common.Msg msg = Common.Msg.newBuilder()
                .setHead(header)
                .setBody(body)
                .build();
        Inner.RouteMsgReq touteReq = Inner.RouteMsgReq.newBuilder()
                .setType(Inner.RouteType.CMD)
                .setMsg(msg)
                .build();
        pushService.routeMsg(touteReq);
        return Outer.ReleaseConnectionsResp.newBuilder().setRet(CommonConstants.SUCCESS).build();
    }

    @Override
    public Outer.RecoverServiceResp recoverService(Outer.RecoverServiceReq req) {
        String cmd = req.getApplicationName() + "_" + req.getIp() + "_" + req.getPort();
        Common.Head header = Common.Head.newBuilder()
                .setMsgType(Common.MsgType.RECOVER_SERVER)
                .build();
        Common.Body body = Common.Body.newBuilder()
                .setContent(cmd)
                .build();
        Common.Msg msg = Common.Msg.newBuilder()
                .setHead(header)
                .setBody(body)
                .build();
        Inner.RouteMsgReq touteReq = Inner.RouteMsgReq.newBuilder()
                .setType(Inner.RouteType.CMD)
                .setMsg(msg)
                .build();
        pushService.routeMsg(touteReq);
        return Outer.RecoverServiceResp.newBuilder().setRet(CommonConstants.SUCCESS).build();
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
