package com.example.connector.netty;

import com.example.connector.entity.domain.ClusterNode;
import com.example.connector.netty.handler.BizHandler;
import com.example.connector.netty.handler.HeartBeatHandler;
import com.example.connector.netty.handler.IdleTrigger;
import com.example.connector.netty.initializer.DefaultInitializer;
import com.example.connector.netty.initializer.SslInitializer;
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
    private volatile boolean start = false;

    public NettyLauncher(ClusterNode localNode) {
        this.localNode = localNode;
    }

    public boolean isStart() {
        return start;
    }

    @Override
    public void run() {
        EpollEventLoopGroup bossGroup = new EpollEventLoopGroup();
        /*
         * 默认可用处理器 * 2
         */
        EpollEventLoopGroup workerGroup = new EpollEventLoopGroup();
        try {
            int port = Integer.parseInt(localNode.getPort());
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(EpollServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new DefaultInitializer());
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
