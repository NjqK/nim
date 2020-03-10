package com.example.netty.study.newclient;

import com.example.netty.study.newclient.handler.ClientBizHandler;
import com.example.netty.study.newclient.handler.ClientRetryConnectHandler;
import com.example.netty.study.newclient.handler.PingerHandler;
import com.example.netty.study.newclient.handler.ReconnectHandler;
import com.example.netty.study.newclient.policy.DefaultRetryPolicy;
import com.example.netty.study.newclient.policy.RetryPolicy;
import com.example.proto.common.common.Common;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-8 下午1:56
 **/
public class TcpClient {

    private String host;
    private int port;
    private Bootstrap bootstrap;
    private EventLoopGroup group = new NioEventLoopGroup();
    /**
     * 重连策略
     */
    private RetryPolicy retryPolicy;
    /**
     * 可用于在其他非handler的地方发送数据
     */
    private Channel channel;
    private TcpClient client;

    public TcpClient(String host, int port) {
        this(host, port, DefaultRetryPolicy.DEFAULT);
    }

    public TcpClient(String host, int port, RetryPolicy retryPolicy) {
        this.host = host;
        this.port = port;
        this.retryPolicy = retryPolicy;
        client = this;
        init();
    }

    /**
     * 向远程TCP服务器请求连接
     */
    public void connect() {
        synchronized (bootstrap) {
            ChannelFuture future = bootstrap.connect(host, port);
            future.addListener(getConnectionListener());
            this.channel = future.channel();
        }
    }

    public void close() {
        synchronized (bootstrap) {
            channel.closeFuture().syncUninterruptibly();
        }
        group.shutdownGracefully();
    }

    public RetryPolicy getRetryPolicy() {
        return retryPolicy;
    }

    private void init() {
        // bootstrap 可重用, 只需在TcpClient实例化的时候初始化即可.
        bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(getChannelInitializer());
    }

    private ChannelInitializer<Channel> getChannelInitializer() {
        return new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                // 半包处理
                pipeline.addLast(new ProtobufVarint32FrameDecoder());
                // 解码的目标类
                pipeline.addLast(new ProtobufDecoder(Common.Msg.getDefaultInstance()));
                pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
                pipeline.addLast(new ProtobufEncoder());
                //pipeline.addLast(new ClientIdleStateTrigger());
                pipeline.addLast(new ReconnectHandler(client));
                pipeline.addLast(new PingerHandler());
                pipeline.addLast(new ClientBizHandler());
            }
        };
    }

    private ChannelFutureListener getConnectionListener() {
        return new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    future.channel().pipeline().fireChannelInactive();
                }
            }
        };
    }

    public static void main(String[] args) {
        TcpClient tcpClient = new TcpClient("192.168.129.128", 33857);
        tcpClient.connect();
    }

}

