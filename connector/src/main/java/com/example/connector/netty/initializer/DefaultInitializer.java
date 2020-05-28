package com.example.connector.netty.initializer;

import com.example.connector.netty.handler.BizHandler;
import com.example.connector.netty.handler.HeartBeatHandler;
import com.example.connector.netty.handler.IdleTrigger;
import com.example.proto.common.common.Common;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-12 下午1:17
 **/
public class DefaultInitializer extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        // 超过60s没有收到客户端消息，TODO 时间改为配置
        pipeline.addLast(new IdleStateHandler(60, 0, 0));
        pipeline.addLast("IdleTriggerHandler", new IdleTrigger());
        // 半包处理
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        // 解码的目标类
        pipeline.addLast("decoder", new ProtobufDecoder(Common.Msg.getDefaultInstance()));
        // 必需要在encoder前
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        // 编码器
        pipeline.addLast("encode", new ProtobufEncoder());
        pipeline.addLast("HeartBeatHandler", new HeartBeatHandler());
        // 逻辑handler
        pipeline.addLast("BizHandler", new BizHandler());
    }
}
