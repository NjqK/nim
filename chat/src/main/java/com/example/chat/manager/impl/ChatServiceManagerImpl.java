package com.example.chat.manager.impl;

import com.example.chat.dao.manager.MsgInfoDaoManager;
import com.example.chat.entity.dto.MsgInfoDto;
import com.example.chat.manager.ChatServiceManager;
import com.example.common.CommonConstants;
import com.example.common.util.UuidGenUtil;
import com.example.proto.common.common.Common;
import com.example.proto.outer.outer.Outer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-9 下午1:19
 **/
@Slf4j
@Service
public class ChatServiceManagerImpl implements ChatServiceManager {

    @Autowired
    private MsgInfoDaoManager msgInfoDaoManager;

    @Override
    public Outer.DoGroupSendingResp doGroupSending(Outer.DoGroupSendingReq req) {
        return null;
    }

    @Override
    public Outer.SendMsgIndividuallyResp sendMsgIndividually(Outer.SendMsgIndividuallyReq req) {
        // TODO 存到消息表然后发给push
        MsgInfoDto msgInfoDto = new MsgInfoDto();
        msgInfoDto.setGuid(1L);
        msgInfoDto.setFromUid(Long.valueOf(req.getFromUid()));
        msgInfoDto.setToUid(Long.valueOf(req.getToUid()));
        msgInfoDto.setMsgType(req.getMsgTypeValue());
        msgInfoDto.setMsgContentType(req.getMsgContentTypeValue());
        msgInfoDto.setGuid(UuidGenUtil.getUUID());
        // TODO 封装消息的类
        msgInfoDto.setMsgBody(Common.Body.newBuilder()
                .setContent("ss")
                .build());
        if (msgInfoDaoManager.addMsgInfo(msgInfoDto) != 1) {
            // TODO return fail
            Outer.SendMsgIndividuallyResp.newBuilder().setRet(CommonConstants.FAIL).build();
        }
        return Outer.SendMsgIndividuallyResp.newBuilder().setRet(CommonConstants.SUCCESS).build();
    }
}
