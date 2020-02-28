package com.example.netty.study.netty;

import com.example.netty.study.common.Constants;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author kuro
 * @version V1.0
 * @date 20-2-28 下午7:08
 **/
public class NettyTimeServer {

    /**
     * bind port
     * @param port
     * @throws Exception
     */
    public void bind(int port) throws Exception {
        // Reactor线程组，分别处理客户端连接和网络读写
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 启动NIO服务端的辅助启动类
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .option(ChannelOption.SO_BACKLOG, Constants.ONE_MB)
                    .childHandler(new MyServerInitializer());
            // 绑定端口，同步等待成功
            ChannelFuture f = b.bind().sync();
            // 等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } finally {
            // 优雅地退出，释放线程资源
            bossGroup.shutdownGracefully().sync();
            workerGroup.shutdownGracefully().sync();
        }
    }

    private class MyServerInitializer extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            /*ch.pipeline().addLast("httpServerCodec",
                    new HttpServerCodec());
            ch.pipeline().addLast("timeServerHandler",
                    new NettyHelloWorldServerHandler());*/
            ch.pipeline().addLast(new NettyTimeServerHandler());
        }
    }

    public static void main(String[] args) throws Exception {
        int port = Constants.DEFAULT_PORT;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException ignored) {

            }
        }
        new NettyTimeServer().bind(port);
    }
}