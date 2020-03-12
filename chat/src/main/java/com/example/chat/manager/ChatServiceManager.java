package com.example.chat.manager;

import com.example.proto.outer.outer.Outer;

/**
 * service逻辑实现的接口
 * @author kuro
 * @version v1.0
 * @date 20-3-9 下午1:14
 **/
public interface ChatServiceManager {

    Outer.DoGroupSendingResp doGroupSending(Outer.DoGroupSendingReq req);

    Outer.SendMsgIndividuallyResp sendMsgIndividually(Outer.SendMsgIndividuallyReq req);

    Outer.GetUnreadMsgResp getUnreadMsg(Outer.GetUnreadMsgReq getUnreadMsgReq);
}
