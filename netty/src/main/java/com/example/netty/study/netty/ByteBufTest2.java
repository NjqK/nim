package com.example.netty.study.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufProcessor;
import io.netty.buffer.Unpooled;
import io.netty.util.ByteProcessor;
import io.netty.util.CharsetUtil;

/**
 * @author kuro
 * @version V1.0
 * @date 20-3-2 下午7:52
 **/
public class ByteBufTest2 {

    public static void main(String[] args) {
        // testRead();
        // testWrite();
        // testIndexManager();
        // findByByteProcessor();
        ByteBuf byteBuf = Unpooled.buffer(100);
        byteBuf.writeBytes("asdfkldsjklf;;;".getBytes());
        ByteBuf byteBuf1 = byteBuf.duplicate();
        //ByteBuf byteBuf1 = Unpooled.wrappedUnmodifiableBuffer(byteBuf);
        printDetail(byteBuf);
        printDetail(byteBuf1);
        byteBuf1.writerIndex(51);
        // 有各自的索引但是内部存储共用
        printDetail(byteBuf);
        printDetail(byteBuf1);
        byteBuf1.setByte(0, 'c');
        System.out.println("内容: " + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("内容: " + byteBuf1.toString(CharsetUtil.UTF_8));
        System.out.println("********************");
    }

    private static void findByByteProcessor() {
        ByteBuf byteBuf = Unpooled.buffer(100);
        byteBuf.writeBytes("asdfkldsjklf;;;".getBytes());
        int i = byteBuf.forEachByte(ByteProcessor.FIND_SEMI_COLON);
        System.out.println(i + " " + (char) byteBuf.getByte(i));
    }

    private static void testIndexManager() {
        ByteBuf byteBuf = Unpooled.buffer(100);
        byteBuf.writeBytes("asdfkldsjklf".getBytes());
        byteBuf.readerIndex(10);
        printDetail(byteBuf);
        //ByteBuf markReaderIndex = byteBuf.markReaderIndex();
        ByteBuf markWriterIndex = byteBuf.markWriterIndex();
        byteBuf.readerIndex(5);
        byteBuf.writerIndex(51);
        printDetail(byteBuf);
        //printDetail(markReaderIndex);
        printDetail(markWriterIndex);
        //System.out.println((byteBuf == markReaderIndex) && (byteBuf == markWriterIndex));
        byteBuf.resetReaderIndex();
        printDetail(byteBuf);
    }

    private static void testWrite() {
        ByteBuf byteBuf = Unpooled.buffer(10);
        // printDetail(byteBuf);

        byteBuf.writeBytes("0123456789".getBytes());
        //printDetail(byteBuf);

        byteBuf.readerIndex(5);
        //printDetail(byteBuf);

        ByteBuf byteBuf2 = Unpooled.buffer(120);
        printDetail(byteBuf2);
        byteBuf2.writeBytes(byteBuf);
        printDetail(byteBuf2);
    }

    private static void printDetail(ByteBuf buf) {
        System.out.println("********************");
        System.out.println("容量: " + buf.capacity());
        System.out.println("可写: " + buf.writableBytes());
        System.out.println("写索引: " + buf.writerIndex());
        System.out.println("读索引: " + buf.readerIndex());
//        System.out.println("内容: ");
//        while (buf.isReadable()) {
//            System.out.printf("%c ", buf.readByte());
//        }
//        System.out.println("********************");
    }

    private static void testRead() {
        ByteBuf byteBuf = Unpooled.wrappedBuffer("asdasdasd".getBytes());
        System.out.println(byteBuf.readerIndex());
        System.out.println((char) byteBuf.getByte(0));
        System.out.println(byteBuf.readerIndex());
        System.out.println(byteBuf.readerIndex(3));
        System.out.println(byteBuf.readerIndex());
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        for (byte b : bytes) {
            System.out.printf("%c ", (char) b);
        }
        System.out.println();
        System.out.println(byteBuf.readerIndex());
        byteBuf = Unpooled.wrappedBuffer("asdasdasd".getBytes());
        while (byteBuf.isReadable()) {
            System.out.printf("%c ", byteBuf.readByte());
        }
    }
}