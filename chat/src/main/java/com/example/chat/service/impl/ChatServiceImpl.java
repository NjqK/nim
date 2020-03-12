package com.example.chat.service.impl;

import com.example.api.inner.inner.ConnectorService;
import com.example.api.inner.inner.PushService;
import com.example.api.outer.outer.ChatService;
import com.example.chat.manager.ChatServiceManager;
import com.example.common.CommonConstants;
import com.example.proto.common.common.Common;
import com.example.proto.inner.inner.Inner;
import com.example.proto.outer.outer.Outer;
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
    public Outer.DoGroupSendingResp doGroupSending(Outer.DoGroupSendingReq req) {
        log.info("doGroupSending, req:{}", req);
        Outer.DoGroupSendingResp.Builder builder = Outer.DoGroupSendingResp.newBuilder();
        try {
            // TODO data check
            // TODO logical implement
            return builder.setRet(CommonConstants.SUCCESS).build();
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
            if (StringUtils.isEmpty(req.getMsgContent())) {
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
}
