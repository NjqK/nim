
package com.example.api.outer.outer;

import com.example.proto.outer.outer.Outer.AckMsgReq;
import com.example.proto.outer.outer.Outer.AckMsgResp;
import com.example.proto.outer.outer.Outer.DoGroupSendingReq;
import com.example.proto.outer.outer.Outer.DoGroupSendingResp;
import com.example.proto.outer.outer.Outer.GetAvailableNodeReq;
import com.example.proto.outer.outer.Outer.GetAvailableNodeResp;
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

    /**
     * 确认收到消息
     *
     * @param ackMsgReq the AckMsgReq
     * @return AckMsgResp
     */
    AckMsgResp ackMsg(AckMsgReq ackMsgReq);

    /**
     * 获取可用的netty服务节点信息
     *
     * @param getAvailableNodeReq the GetAvailableNodeReq
     * @return GetAvailableNodeResp
     */
    GetAvailableNodeResp getAvailableNode(GetAvailableNodeReq getAvailableNodeReq);

}

/**
 * Revision history
 * -------------------------------------------------------------------------
 *
 * Date Author Note
 * -------------------------------------------------------------------------
 * 2020-03-08 kuro create
 */
