package com.example.connector.netty;

import com.example.connector.entity.cluster.ClusterNode;
import com.example.connector.netty.handler.BizHandler;
import com.example.connector.netty.handler.HeartBeatHandler;
import com.example.proto.common.common.Common;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-7 下午4:17
 **/
@Slf4j
public class NettyLauncher implements Runnable {

    private ClusterNode localNode;

    /**
     * netty绑定完成
     */
    private boolean start = false;

    public NettyLauncher(ClusterNode localNode) {
        this.localNode = localNode;
    }

    public boolean isStart() {
        return start;
    }

    @Override
    public void run() {
        EpollEventLoopGroup bossGroup = new EpollEventLoopGroup();
        EpollEventLoopGroup workerGroup = new EpollEventLoopGroup();
        try {
            int port = Integer.parseInt(localNode.getPort());
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(EpollServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .option(ChannelOption.SO_BACKLOG, 1024)
//                        .option(ChannelOption.TCP_NODELAY, true)
//                        .option(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 超过5s没有收到客户端消息，TODO 改为配置
                            pipeline.addLast(new IdleStateHandler(30, 0, 0));
                            // 半包处理
                            pipeline.addLast(new ProtobufVarint32FrameDecoder());
                            // 解码的目标类
                            pipeline.addLast(new ProtobufDecoder(Common.Msg.getDefaultInstance()));
                            // 必修要在encoder前
                            pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
                            // 编码器
                            pipeline.addLast(new ProtobufEncoder());
                            pipeline.addLast(new HeartBeatHandler());
                            // 逻辑handler
                            pipeline.addLast(new BizHandler());
                        }
                    });
            ChannelFuture future = b.bind().sync();
            future.addListener((ChannelFutureListener) future1 -> {
                // 启动成功
                start = true;
            });
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("创建Netty服务器失败，error:{}", e.getMessage());
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
