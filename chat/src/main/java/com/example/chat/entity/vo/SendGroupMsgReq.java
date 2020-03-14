package com.example.chat.entity.vo;

import lombok.Data;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-14 下午12:31
 **/
@Data
public class SendGroupMsgReq {
    /**
     * 目标用户
     */
    private String toUids;
    /**
     * 发送者
     */
    private String fromUid;
    /**
     * 消息内容
     */
    private String msgContent;
    /**
     * 消息内容类型
     */
    private String msgContentType;
}
