package com.example.netty.study.netty;

import com.example.netty.study.common.Constants;
import com.example.proto.common.common.Common;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;

/**
 * @author kuro
 * @version V1.0
 * @date 20-2-28 下午8:44
 **/
public class NettyTimeClient {

    private String host;
    private int port;
    public final static AttributeKey<Integer> id = AttributeKey.newInstance("ID");

    public NettyTimeClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .remoteAddress("192.168.129.137", 40623)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //ch.pipeline().addLast(new NettyTimeClientHandler());
                            ChannelPipeline pipeline = ch.pipeline();
                            // 半包处理
                            pipeline.addLast(new ProtobufVarint32FrameDecoder());
                            // 解码的目标类
                            pipeline.addLast(new ProtobufDecoder(Common.Msg.getDefaultInstance()));
                            pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
                            pipeline.addLast(new ProtobufEncoder());
                            pipeline.addLast(new ProtoTestHandler());
                        }
                    });
            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        new NettyTimeClient(Constants.DEFAULT_HOST, Constants.DEFAULT_PORT).start();
    }
}