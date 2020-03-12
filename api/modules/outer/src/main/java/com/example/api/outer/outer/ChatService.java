
package com.example.api.outer.outer;

import com.example.proto.outer.outer.Outer.DoGroupSendingReq;
import com.example.proto.outer.outer.Outer.DoGroupSendingResp;
import com.example.proto.outer.outer.Outer.GetUnreadMsgReq;
import com.example.proto.outer.outer.Outer.GetUnreadMsgResp;
import com.example.proto.outer.outer.Outer.SendMsgIndividuallyReq;
import com.example.proto.outer.outer.Outer.SendMsgIndividuallyResp;

/**
 * @Type ChatService.java
 * @Desc The ChatService
 * @author kuro
 * @date  12:00:00
 * @version
 */
public interface ChatService {

    /**
     * 群发
     *
     * @param doGroupSendingReq the DoGroupSendingReq
     * @return DoGroupSendingResp
     */
    DoGroupSendingResp doGroupSending(DoGroupSendingReq doGroupSendingReq);

    /**
     * 拉取消息
     *
     * @param getUnreadMsgReq the GetUnreadMsgReq
     * @return GetUnreadMsgResp
     */
    GetUnreadMsgResp getUnreadMsg(GetUnreadMsgReq getUnreadMsgReq);

    /**
     * 单发消息
     *
     * @param sendMsgIndividuallyReq the SendMsgIndividuallyReq
     * @return SendMsgIndividuallyResp
     */
    SendMsgIndividuallyResp sendMsgIndividually(SendMsgIndividuallyReq sendMsgIndividuallyReq);

}

/**
 * Revision history
 * -------------------------------------------------------------------------
 *
 * Date Author Note
 * -------------------------------------------------------------------------
 * 2020-03-08 kuro create
 */
