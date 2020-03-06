package com.example.netty.study.netty;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author kuro
 * @version V1.0
 * @date 3/1/20 8:19 PM
 **/
public class ByteBufTest {

    public static void main(String[] args) {
        new ByteBufTest().onStart();

        CompositeByteBuf c = Unpooled.compositeBuffer();
        c.addComponents(Unpooled.wrappedBuffer(new byte[] {})
                , Unpooled.wrappedBuffer(new byte[] {}));
        c.removeComponent(0);
        for (ByteBuf buf : c) {
            System.out.println(c.toString());
        }
        // CompositeByteBuf可能不支持访问其支撑数组，可以通过可以通过下面的方法
        CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();
        int length = compositeByteBuf.readableBytes();
        byte[] array = new byte[length];
        compositeByteBuf.getBytes(compositeByteBuf.readerIndex(), array);

    }

    private void onStart() {
        ByteBuf buf = Unpooled.wrappedBuffer(new byte[]{'q', 'b'});
        System.out.println(JSON.toJSONString(buf));
        // 检查ByteBuf是否有一个支撑数组
        if (buf.hasArray()) {
            byte[] array = buf.array();
            int offset = buf.arrayOffset() + buf.readerIndex();
            int len = buf.readableBytes();
            handlerArray(array, offset, len, buf);
        } else {
            int length = buf.readableBytes();
            byte[] array = new byte[length];
            buf.getBytes(buf.readerIndex(), array);
        }
    }

    private void handlerArray(byte[] array, int offset, int length, ByteBuf buf) {
        System.out.println("offset: " + offset + ", len: " + length);
        if (length > 0) {
            array[offset] = 'c';
        }
        System.out.println(JSON.toJSONString(buf));
    }
}