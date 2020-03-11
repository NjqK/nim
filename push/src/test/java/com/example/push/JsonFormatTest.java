package com.example.push;

import com.example.proto.common.common.Common;
import com.example.proto.inner.inner.Inner;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-11 下午1:27
 **/
public class JsonFormatTest {

    public static void main(String[] args) throws InvalidProtocolBufferException {
        Inner.RouteMsgReq req = Inner.RouteMsgReq.newBuilder()
                .setToUid("1")
                .setMsg(Common.Msg.newBuilder()
//                        .setHead()
//                        .setBody()
                        .build())
                .build();
        String print = JsonFormat.printer().print(req);
        System.out.println(print);
        Inner.RouteMsgReq.Builder req1 = Inner.RouteMsgReq.newBuilder();
        JsonFormat.parser().merge(print, req1);
        System.out.println(req1.build());
    }
}
