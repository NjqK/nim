package com.example.netty.study.netty;

import com.example.netty.study.common.Constants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @author kuro
 * @version V1.0
 * @date 20-2-28 下午8:47
 **/
@ChannelHandler.Sharable
public class NettyTimeClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与服务器建立连接...");
        ctx.writeAndFlush(Unpooled.copiedBuffer(Constants.DEFAULT_COMMAND, CharsetUtil.UTF_8));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        // 服务器发出的数据可能分块接收，被多次调用
        System.out.println("收到服务端消息: " + msg.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}