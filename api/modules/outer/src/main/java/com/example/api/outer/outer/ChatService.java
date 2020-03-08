
package com.example.api.outer.outer;

import com.example.proto.outer.outer.Outer.SendMsgIndividuallyReq;
import com.example.proto.outer.outer.Outer.SendMsgIndividuallyResp;

/**
 * @Type ChatService.java
 * @Desc The ChatService
 * @author jay
 * @date  12:00:00
 * @version
 */
public interface ChatService {

    /**
     * 群发
     *
     * @param sendMsgIndividuallyReq the SendMsgIndividuallyReq
     * @return SendMsgIndividuallyResp
     */
    SendMsgIndividuallyResp doGroupSending(SendMsgIndividuallyReq sendMsgIndividuallyReq);

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
 * 2019-08-08 jay create
 */
