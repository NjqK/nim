package com.example.connector.netty.initializer;

import com.example.connector.netty.handler.BizHandler;
import com.example.connector.netty.handler.HeartBeatHandler;
import com.example.connector.netty.handler.IdleTrigger;
import com.example.connector.netty.ssl.SSLContextFactory;
import com.example.proto.common.common.Common;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleStateHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-7 下午6:45
 **/
public class SslInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        SSLContext sslContext = SSLContextFactory.getServerContext();
        //设置为服务器模式
        SSLEngine sslEngine = sslContext.createSSLEngine();
        sslEngine.setUseClientMode(false);
        //是否需要验证客户端 。 如果是双向认证，则需要将其设置为true，同时将client证书添加到server的信任列表中
        sslEngine.setNeedClientAuth(true);
        ch.pipeline().addLast("ssl", new SslHandler(sslEngine));
        // 超过5s没有收到客户端消息，TODO 时间改为配置
        pipeline.addLast(new IdleStateHandler(60, 0, 0));
        pipeline.addLast("IdleTriggerHandler", new IdleTrigger());
        // 半包处理
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        // 解码的目标类
        pipeline.addLast("decoder", new ProtobufDecoder(Common.Msg.getDefaultInstance()));
        // 必修要在encoder前
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        // 编码器
        pipeline.addLast("encode", new ProtobufEncoder());
        pipeline.addLast("HeartBeatHandler", new HeartBeatHandler());
        // 逻辑handler
        pipeline.addLast("BizHandler", new BizHandler());
    }
}
