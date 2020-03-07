package com.example.connector.netty.handler;

import com.example.proto.common.common.Common;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-7 下午3:12
 **/
@Slf4j
public class TestHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Common.Msg message = (Common.Msg) msg;
        log.info(getClass().getName() + " got msg:{}", message);
        ctx.writeAndFlush(defaultMsg());
    }

    private Common.Msg defaultMsg() {
        Common.Msg.Builder builder = Common.Msg.newBuilder();
        Common.Head header = Common.Head.newBuilder()
                .setMsgType(Common.MsgType.SINGLE_CHAT)
                .setMsgContentType(Common.MsgContentType.TEXT)
                .setTimestamp(111L)
                .setStatusReport(1)
                .build();
        Common.Body body = Common.Body.newBuilder()
                .setContent("msg received")
                .build();
        builder.setHead(header);
        builder.setBody(body);
        return builder.build();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
