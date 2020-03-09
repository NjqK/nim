package com.example.chat.manager.impl;

import com.example.chat.dao.manager.MsgInfoDaoManager;
import com.example.chat.manager.ChatServiceManager;
import com.example.common.CommonConstants;
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

        return Outer.SendMsgIndividuallyResp.newBuilder().setRet(CommonConstants.SUCCESS).build();
    }
}
