package com.example.chat;

import com.example.proto.common.common.Common;
import com.example.proto.inner.inner.Inner;
import com.google.protobuf.InvalidProtocolBufferException;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-10 下午10:49
 **/
public class ProtoStuffTest {

    public static void main(String[] args) throws InvalidProtocolBufferException {
        // methodA();
        // methodB();
        methodC();
    }

    private static void methodC() {
        Common.A a = Common.A.newBuilder().setA("a").setB("c").build();
        Common.A b = Common.A.newBuilder().setA("a").setB("c").build();
        Common.B bB = Common.B.newBuilder().addAList(a).addAList(b).build();
        Schema<Common.B> schema = RuntimeSchema.getSchema(Common.B.class);
        LinkedBuffer buffer = LinkedBuffer.allocate(512);
        byte[] protostuff = ProtostuffIOUtil.toByteArray(bB, schema, buffer);
        Common.B routeMsgReq = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(protostuff, routeMsgReq, schema);
        System.out.println(routeMsgReq);
    }

    private static void methodB() {
        Common.A a = Common.A.newBuilder().setA("a").setB("c").build();
        Schema<Common.A> schema = RuntimeSchema.getSchema(Common.A.class);
        LinkedBuffer buffer = LinkedBuffer.allocate(512);
        byte[] protostuff = ProtostuffIOUtil.toByteArray(a, schema, buffer);
        Common.A routeMsgReq = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(protostuff, routeMsgReq, schema);
        System.out.println(routeMsgReq);
    }

    private static void methodA() throws InvalidProtocolBufferException {
        Common.ExtraHeader extra = Common.ExtraHeader.newBuilder().setKey("key").setValue("value").build();
        Common.Head header = Common.Head.newBuilder()
                .setMsgId(1L)
                .setMsgType(Common.MsgType.SINGLE_CHAT)
                .setMsgContentType(Common.MsgContentType.TEXT)
                .addExtends(extra)
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
        Inner.RouteMsgReq routeMsgReq = Inner.RouteMsgReq.parseFrom(req.toByteArray());
        System.out.println(routeMsgReq);
//        Schema<Inner.RouteMsgReq> schema = RuntimeSchema.getSchema(Inner.RouteMsgReq.class);
//        LinkedBuffer buffer = LinkedBuffer.allocate(512);
//        byte[] protostuff = ProtostuffIOUtil.toByteArray(req, schema, buffer);
//        Inner.RouteMsgReq routeMsgReq = schema.newMessage();
//        ProtostuffIOUtil.mergeFrom(protostuff, routeMsgReq, schema);
//        System.out.println(routeMsgReq);
    }
}
