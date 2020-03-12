package com.example.chat;

import com.example.proto.common.common.Common;
import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.util.CharsetUtil;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-12 下午8:17
 **/
public class ByteToProtoTest {
    public static void main(String[] args) throws InvalidProtocolBufferException {
        Common.Body body = Common.Body.newBuilder().setContent("123123").build();
        byte[] bodyByteArray = body.toByteArray();
        Common.Body body1 = Common.Body.parseFrom(bodyByteArray);
        System.out.println(body);
        System.out.println(body1);
    }
}
