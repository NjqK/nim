package com.example.netty.study.netty;

import com.alibaba.fastjson.JSON;
import io.netty.channel.*;

/**
 * @author kuro
 * @version V1.0
 * @date 20-2-29 下午8:30
 **/
@ChannelHandler.Sharable
public class NettyChannelOutBoundHandlerTest extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("got msgBody" + JSON.toJSON(msg));
        ctx.writeAndFlush(msg);
    }
}
//public class NettyChannelOutBoundHandlerTest extends ChannelInboundHandlerAdapter {
//
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msgBody) throws Exception {
//        super.channelRead(ctx, msgBody);
//        System.out.println(JSON.toJSON(msgBody));
//    }
//}