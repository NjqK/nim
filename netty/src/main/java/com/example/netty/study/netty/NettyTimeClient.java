package com.example.netty.study.netty;

import com.example.netty.study.common.Constants;
import com.example.netty.study.netty.listener.ClientConnector;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kuro
 * @version V1.0
 * @date 20-2-28 下午8:44
 **/
@Slf4j
public class NettyTimeClient {

    private String host;
    private int port;

    public NettyTimeClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .remoteAddress("192.168.129.137", 45755)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
//                            //ch.pipeline().addLast(new NettyTimeClientHandler());
//                            ChannelPipeline pipeline = ch.pipeline();
//                            // 半包处理
//                            pipeline.addLast(new ProtobufVarint32FrameDecoder());
//                            // 解码的目标类
//                            pipeline.addLast(new ProtobufDecoder(Common.Msg.getDefaultInstance()));
//                            pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
//                            pipeline.addLast(new ProtobufEncoder());
//                            pipeline.addLast(new ClientRetryConnectHandler());
//                            //pipeline.addLast(new ClientIdleStateTrigger());
//                            //pipeline.addLast(new PingerHandler());
//                            pipeline.addLast(new ClientBizHandler());
                        }
                    });
            ChannelFuture f = new ClientConnector(b).connect();
            if (f != null) {
                f.channel().closeFuture().sync();
            }
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        new NettyTimeClient(Constants.DEFAULT_HOST, Constants.DEFAULT_PORT).start();
    }
}