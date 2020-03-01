package com.example.netty.study.netty;


import com.example.netty.study.common.Constants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.Date;

/**
 * 一个ChannelHandler可以被多个Channel安全共享，可以使用同样的实例
 * @author kuro
 * @version V1.0
 * @date 20-2-28 下午8:09
 **/
@ChannelHandler.Sharable
public class NettyTimeServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        String body = in.toString(CharsetUtil.UTF_8);
        System.out.println("服务器接收到: " + body);
        String currentTime = Constants.DEFAULT_COMMAND.equalsIgnoreCase(body)
                ? new Date(System.currentTimeMillis()).toString()
                : Constants.DEFAULT_RESP;
        // 返回消息给客户端
        System.out.println("发送数据: " + currentTime);
        ByteBuf out = Unpooled.wrappedBuffer(currentTime.getBytes());
        ctx.write(out);
        // 传到下一个channel
        // ctx.fireChannelRead(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}