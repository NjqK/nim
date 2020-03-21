package com.example.core;

import com.alibaba.fastjson.JSON;
import com.example.entity.InputDataPojo;
import com.example.proto.common.common.Common;
import com.google.protobuf.util.JsonFormat;
import org.apache.jmeter.protocol.tcp.sampler.ReadException;
import org.apache.jmeter.protocol.tcp.sampler.TCPClientImpl;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.util.JOrphanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 心跳测试类
 */
public class HeartBeatTcpSampler extends TCPClientImpl {

    private static final Logger log = LoggerFactory.getLogger(HeartBeatTcpSampler.class);

    private static final Lock LOCK = new ReentrantLock();

    private static volatile Common.Msg heartBeat = null;

    /**
     * TODO 修改硬编码
     * PING PONG的长度只有4字节，前缀1字节
     */
    private static final int lengthPrefixLen = 1;

    @Override
    public void write(OutputStream os, String s) throws IOException {
        if (heartBeat == null) {
            synchronized (LOCK) {
                if (heartBeat == null) {
                    InputDataPojo mapType = JSON.parseObject(s, InputDataPojo.class);
                    Common.Head head = Common.Head.newBuilder()
                            .setMsgType(Common.MsgType.valueOf(mapType.getMsgType()))
                            .build();
                    heartBeat = Common.Msg.newBuilder()
                            .setHead(head)
                            .build();
                }
            }
        }
        byte[] bodyBytes = heartBeat.toByteArray();
        int bodyLength = bodyBytes.length;
        int headerLen = computeRawVarint32Size(bodyLength);
        byte[] output = new byte[headerLen + bodyLength];
        int begin = writeRawVarint32(output, bodyLength);
        for (byte b : bodyBytes) {
            output[begin++] = b;
        }
        os.write(output);
        os.flush();
    }

    /**
     * Computes size of protobuf varint32 after encoding.
     *
     * @param value which is to be encoded.
     * @return size of value encoded as protobuf varint32.
     */
    private int computeRawVarint32Size(final int value) {
        if ((value & (0xffffffff << 7)) == 0) {
            return 1;
        }
        if ((value & (0xffffffff << 14)) == 0) {
            return 2;
        }
        if ((value & (0xffffffff << 21)) == 0) {
            return 3;
        }
        if ((value & (0xffffffff << 28)) == 0) {
            return 4;
        }
        return 5;
    }

    private int writeRawVarint32(byte[] bytes, int value) {
        int index = 0;
        while (true) {
            if ((value & ~0x7F) == 0) {
                bytes[index++] = (byte) value;
                return index;
            } else {
                int i = (value & 0x7F) | 0x80;
                bytes[index++] = (byte) value;
                value >>>= 7;
            }
        }
    }

    @Override
    public void write(OutputStream os, InputStream is) throws IOException {
        super.write(os, is);
    }

    @Override
    public String read(InputStream is, SampleResult sampleResult) throws ReadException {
        byte[] msg = new byte[0];
        int msgLen = 0;
        byte[] lengthBuffer = new byte[lengthPrefixLen];
        try {
            if (is.read(lengthBuffer, 0, lengthPrefixLen) == lengthPrefixLen) {
                sampleResult.latencyEnd();
                msgLen = lengthBuffer[0];
                msg = new byte[msgLen];
                int bytes = JOrphanUtils.read(is, msg, 0, msgLen);
                if (bytes < msgLen) {
                    log.warn("Incomplete message read, expected: {} got: {}", msgLen, bytes);
                }
            }
            Common.Msg resp = Common.Msg.parseFrom(msg);
            return JsonFormat.printer().print(resp);
        }
        catch(IOException e) {
            throw new ReadException("", e, JOrphanUtils.baToHexString(msg));
        }
//        try {
//            byte[] buffer = new byte[4096];
//            int x;
//            boolean first = true;
//            is.read(buffer);
//            //readRawVarint32(buffer);
//            log.error("resp:{}", buffer);
//            buffer = null;
//            //Common.Msg msg = Common.Msg.parseFrom(buffer);
//            //return JsonFormat.printer().print(msg);
//            return "ok";
//        } catch (IOException e) {
//            throw new RuntimeException("Error read, error:" + e);
//        }
    }

    @Override
    public String read(InputStream is) throws ReadException {
        return read(is, new SampleResult());
    }

//    private static int readRawVarint32(byte[] buffer) {
//        byte tmp = buffer.readByte();
//        if (tmp >= 0) {
//            return tmp;
//        } else {
//            int result = tmp & 127;
//            if (!buffer.isReadable()) {
//                buffer.resetReaderIndex();
//                return 0;
//            }
//            if ((tmp = buffer.readByte()) >= 0) {
//                result |= tmp << 7;
//            } else {
//                result |= (tmp & 127) << 7;
//                if (!buffer.isReadable()) {
//                    buffer.resetReaderIndex();
//                    return 0;
//                }
//                if ((tmp = buffer.readByte()) >= 0) {
//                    result |= tmp << 14;
//                } else {
//                    result |= (tmp & 127) << 14;
//                    if (!buffer.isReadable()) {
//                        buffer.resetReaderIndex();
//                        return 0;
//                    }
//                    if ((tmp = buffer.readByte()) >= 0) {
//                        result |= tmp << 21;
//                    } else {
//                        result |= (tmp & 127) << 21;
//                        if (!buffer.isReadable()) {
//                            buffer.resetReaderIndex();
//                            return 0;
//                        }
//                        result |= (tmp = buffer.readByte()) << 28;
//                        if (tmp < 0) {
//                            throw new CorruptedFrameException("malformed varint.");
//                        }
//                    }
//                }
//            }
//            return result;
//        }
//    }
}
