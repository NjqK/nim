package com.example.chat;

import com.example.proto.common.common.Common;
import com.example.proto.inner.inner.Inner;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.junit.jupiter.api.Test;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-10 下午10:49
 **/
public class ProtoStuffTest {

    public static void main(String[] args) {
        Common.Head header = Common.Head.newBuilder()
                .setMsgId(1L)
                .setMsgType(Common.MsgType.SINGLE_CHAT)
                .setMsgContentType(Common.MsgContentType.TEXT)
                .build();
        Common.Body body = Common.Body.newBuilder()
                .setContent("hello")
                .build();
        Inner.RouteMsgReq req = Inner.RouteMsgReq.newBuilder()
                .setToUid("1")
                .setMsg(Common.Msg.newBuilder()
                        .setHead(header)
                        .setBody(body)
                        .build())
                .build();
        Schema<Inner.RouteMsgReq> schema = RuntimeSchema.getSchema(Inner.RouteMsgReq.class);
        LinkedBuffer buffer = LinkedBuffer.allocate(512);
        byte[] protostuff = ProtostuffIOUtil.toByteArray(req, schema, buffer);
        Inner.RouteMsgReq routeMsgReq = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(protostuff, routeMsgReq, schema);
        System.out.println(routeMsgReq);
    }
}
