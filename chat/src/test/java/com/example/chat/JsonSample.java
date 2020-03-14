package com.example.chat;

import com.alibaba.fastjson.JSON;
import com.example.chat.entity.vo.SendGroupMsgReq;
import com.example.proto.common.common.Common;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-14 下午12:43
 **/
public class JsonSample {
    public static void main(String[] args) {
        SendGroupMsgReq req = new SendGroupMsgReq();
        req.setToUids("1,23,2");
        req.setFromUid("22");
        req.setMsgContent("2233");
        req.setMsgContentType(Common.MsgContentType.MSG_CONTENT_NUL.name());
        System.out.println(JSON.toJSONString(req, true));
    }
}
