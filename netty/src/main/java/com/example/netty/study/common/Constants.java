package com.example.netty.study.common;

import com.example.proto.common.common.Common;

import java.util.ArrayList;

/**
 * @author kuro
 * @version V1.0
 * @date 20-2-26 下午8:31
 **/
public class Constants {

    public static final String DEFAULT_COMMAND = "QUERY TIME ORDER";
    public static final String DEFAULT_RESP = "BAD ORDER";
    public static final int DEFAULT_PORT = 8080;
    public static final String DEFAULT_HOST = "127.0.0.1";
    public static final int ONE_MB = 1024;

    /**
     * TODO 移到Constants里
     */
    public static final Common.Msg PING = Common.Msg.newBuilder()
            .setHead(Common.Head.newBuilder()
                    .setMsgType(Common.MsgType.HEART_BEAT)
                    .build())
            .build();

    /**
     * TODO 移到Constants里
     */
    public static final Common.Msg HAND_SHAKE = Common.Msg.newBuilder()
            .setHead(Common.Head.newBuilder()
                    .setMsgType(Common.MsgType.HAND_SHAKE)
                    .addExtends(Common.ExtraHeader.newBuilder().setKey("uid").setValue("1").build())
                    .build())
            .build();
}