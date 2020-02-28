package com.example.netty.study.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @author kuro
 * @version V1.0
 * @date 20-2-28 下午7:35
 **/
public class NettyHelloWorldServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
     * 0. 读取客户端发过来的请求，并将响应返回给客户端的方法
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        if(msg instanceof HttpRequest) {
            /**
             * 1. 构建响应的字符串信息
             */
            ByteBuf content = Unpooled.copiedBuffer("hello world", CharsetUtil.UTF_8);

            /**
             * 2. 构建FullHttpResponse 对象
             */
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

            /**
             * 3. 设置 response 的头信息
             */
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            /**
             * 4. 将response对象返回到客户端
             */
            ctx.writeAndFlush(response);
        }
    }

}