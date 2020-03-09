package com.example.connector.common;

import com.example.proto.common.common.Common;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-7 下午1:08
 **/
public class Constants {
    /**
     * Default Pong msgBody
     */
    public static final Common.Msg PONG = Common.Msg.newBuilder()
            .setHead(Common.Head.newBuilder()
                    .setMsgType(Common.MsgType.HEART_BEAT)
                    .build())
            .build();

}
