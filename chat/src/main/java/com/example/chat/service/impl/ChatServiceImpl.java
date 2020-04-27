package com.example.chat.service.impl;

import com.example.api.inner.inner.ConnectorService;
import com.example.api.inner.inner.PushService;
import com.example.api.outer.outer.ChatService;
import com.example.chat.manager.ChatServiceManager;
import com.example.common.CommonConstants;
import com.example.common.util.ListUtil;
import com.example.proto.common.common.Common;
import com.example.proto.inner.inner.Inner;
import com.example.proto.outer.outer.Outer;
import com.google.protobuf.util.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-8 下午11:04
 **/
@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatServiceManager chatServiceManager;

    @Reference(version = "1.0.0")
    private ConnectorService connectorService;

    @Override
    public Outer.GetUnreadMsgResp getUnreadMsg(Outer.GetUnreadMsgReq req) {
        log.info("getUnreadMsg, req:{}", req);
        Outer.GetUnreadMsgResp.Builder builder = Outer.GetUnreadMsgResp.newBuilder();
        try {
            // data check
            if (StringUtils.isEmpty(req.getUid())) {
                Common.ErrorMsg errorMsg = Common.ErrorMsg.newBuilder()
                        .setErrorCode(Common.ErrCode.GET_UNREAD_MSG_USER_ID_NUL)
                        .setMsg("目标用户id错误")
                        .build();
                return builder.setRet(errorMsg).build();
            }
            // guid是递增的
            if (StringUtils.isEmpty(req.getMaxGuid())) {
                req = req.toBuilder().setMaxGuid(String.valueOf(Long.MIN_VALUE)).build();
            }
            Outer.GetUnreadMsgResp resp = chatServiceManager.getUnreadMsg(req);
            log.info("getUnreadMsg, resp:{}", resp);
            return resp;
        } catch (Exception e) {
            log.error("getUnreadMsg caught exception, e:{}", e);
            return builder.setRet(CommonConstants.FAIL).build();
        }
    }

    @Override
    public Outer.ReleaseConnectionsResp releaseConnections(Outer.ReleaseConnectionsReq req) {
        log.info("releaseConnections, req:{}", req);
        Outer.ReleaseConnectionsResp.Builder builder = Outer.ReleaseConnectionsResp.newBuilder();
        try {
            if (StringUtils.isEmpty(req.getApplicationName())
                    || StringUtils.isEmpty(req.getIp())
                    || StringUtils.isEmpty(req.getPort())) {
                return builder.setRet(Common.ErrorMsg.newBuilder()
                        .setErrorCode(Common.ErrCode.RELEASE_CONNECTIONS_FAULTY_PARAMETER)
                        .setMsg("参数错误")
                        .build())
                        .build();
            }
            Outer.ReleaseConnectionsResp resp = chatServiceManager.releaseConnections(req);
            log.info("releaseConnections, resp:{}", resp);
            return resp;
        } catch (Exception e) {
            log.error("releaseConnections caught exception, e:{}", e);
            return builder.setRet(CommonConstants.FAIL).build();
        }
    }

    @Override
    public Outer.DoGroupSendingResp doGroupSending(Outer.DoGroupSendingReq req) {
        log.info("doGroupSending, req:{}", req);
        Outer.DoGroupSendingResp.Builder builder = Outer.DoGroupSendingResp.newBuilder();
        try {
            // data check
            if (ListUtil.isEmpty(req.getToUidsList())) {
                Common.ErrorMsg errorMsg = Common.ErrorMsg.newBuilder()
                        .setErrorCode(Common.ErrCode.SEND_MSG_GROUP_TO_UID_NUL)
                        .setMsg("目标用户id错误")
                        .build();
                return builder.setRet(errorMsg).build();
            }
            if (StringUtils.isEmpty(req.getFromUid())) {
                Common.ErrorMsg errorMsg = Common.ErrorMsg.newBuilder()
                        .setErrorCode(Common.ErrCode.SEND_MSG_GROUP_FROM_ID_NUL)
                        .setMsg("发送者id错误")
                        .build();
                return builder.setRet(errorMsg).build();
            }
            if (StringUtils.isEmpty(req.getMsgContent())
                    || Common.MsgContentType.MSG_CONTENT_NUL.equals(req.getMsgContentType())
                    || !Common.MsgType.MULTI_CHAT.equals(req.getMsgType())) {
                Common.ErrorMsg errorMsg = Common.ErrorMsg.newBuilder()
                        .setErrorCode(Common.ErrCode.SEND_MSG_INDIVIDUALLY_MSG_NUL)
                        .setMsg("发送消息错误")
                        .build();
                return builder.setRet(errorMsg).build();
            }
            // TODO logical implement
            Outer.DoGroupSendingResp resp = chatServiceManager.doGroupSending(req);
            log.info("doGroupSending, resp:{}", resp);
            return resp;
        } catch (Exception e) {
            log.error("doGroupSending caught exception, e:{}", e);
            return builder.setRet(CommonConstants.FAIL).build();
        }
    }

    @Override
    public Outer.SendMsgIndividuallyResp sendMsgIndividually(Outer.SendMsgIndividuallyReq req) {
        log.info("sendMsgIndividually, req:{}", req);
        Outer.SendMsgIndividuallyResp.Builder builder = Outer.SendMsgIndividuallyResp.newBuilder();
        try {
            if (StringUtils.isEmpty(req.getToUid())) {
                Common.ErrorMsg errorMsg = Common.ErrorMsg.newBuilder()
                        .setErrorCode(Common.ErrCode.SEND_MSG_INDIVIDUALLY_TO_UID_NUL)
                        .setMsg("目标用户id错误")
                        .build();
                return builder.setRet(errorMsg).build();
            }
            if (StringUtils.isEmpty(req.getFromUid())) {
                Common.ErrorMsg errorMsg = Common.ErrorMsg.newBuilder()
                        .setErrorCode(Common.ErrCode.SEND_MSG_INDIVIDUALLY_FROM_ID_NUL)
                        .setMsg("发送者id错误")
                        .build();
                return builder.setRet(errorMsg).build();
            }
            if (StringUtils.isEmpty(req.getMsgContent())
                    || Common.MsgContentType.MSG_CONTENT_NUL.equals(req.getMsgContentType())
                    || !Common.MsgType.SINGLE_CHAT.equals(req.getMsgType())) {
                Common.ErrorMsg errorMsg = Common.ErrorMsg.newBuilder()
                        .setErrorCode(Common.ErrCode.SEND_MSG_INDIVIDUALLY_MSG_NUL)
                        .setMsg("发送消息错误")
                        .build();
                return builder.setRet(errorMsg).build();
            }
            // logical implement
            Outer.SendMsgIndividuallyResp resp = chatServiceManager.sendMsgIndividually(req);
            log.info("sendMsgIndividually, resp:{}", resp);
            return resp;
        } catch (Exception e) {
            log.error("sendMsgIndividually caught exception, e:{}", e);
            return builder.setRet(CommonConstants.FAIL).build();
        }
    }

    @Override
    public Outer.AckMsgResp ackMsg(Outer.AckMsgReq req) {
        log.info("ackMsg, req:{}", req);
        Outer.AckMsgResp.Builder builder = Outer.AckMsgResp.newBuilder();
        try {
            if (StringUtils.isEmpty(req.getUid())) {
                Common.ErrorMsg errorMsg = Common.ErrorMsg.newBuilder()
                        .setErrorCode(Common.ErrCode.ACK_MSG_RESP_UID_NUL)
                        .setMsg("确认消息uid空")
                        .build();
                return builder.setRet(errorMsg).build();
            }
            if (StringUtils.isEmpty(req.getGuid())) {
                Common.ErrorMsg errorMsg = Common.ErrorMsg.newBuilder()
                        .setErrorCode(Common.ErrCode.ACK_MSG_RESP_GUID_NUL)
                        .setMsg("确认消息guid空")
                        .build();
                return builder.setRet(errorMsg).build();
            }
            Outer.AckMsgResp resp = chatServiceManager.ackMsg(req);
            log.info("ackMsg, resp:{}", resp);
            return resp;
        } catch (Exception e) {
            log.error("ackMsg caught exception, e:{}", e);
            return builder.setRet(CommonConstants.FAIL).build();
        }
    }

    @Override
    public Outer.GetAvailableNodeResp getAvailableNode(Outer.GetAvailableNodeReq req) {
        log.info("getAvailableNode, req:{}", req);
        Outer.GetAvailableNodeResp.Builder builder = Outer.GetAvailableNodeResp.newBuilder();
        try {
            Inner.GetNodeAddresssReq nodeReq = Inner.GetNodeAddresssReq.newBuilder().build();
            Inner.GetNodeAddresssResp nodeResp = connectorService.getNodeAddress(nodeReq);
            log.info("NodeAddress:{}", nodeResp);
            if (nodeResp.getRet().getErrorCode().equals(Common.ErrCode.SUCCESS)) {
                builder.setRet(CommonConstants.SUCCESS);
                builder.setHost(nodeResp.getHost());
                builder.setPort(nodeResp.getPort());
                Outer.GetAvailableNodeResp resp = builder.build();
                log.info("getAvailableNode, resp:{}", resp);
                return resp;
            }
            return builder.setRet(CommonConstants.FAIL).build();
        } catch (Exception e) {
            log.error("getAvailableNode caught exception, e:{}", e);
            return builder.setRet(CommonConstants.FAIL).build();
        }
    }
}
