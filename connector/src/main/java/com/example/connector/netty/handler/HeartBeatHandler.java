package com.example.connector.netty.handler;

import com.example.connector.common.Constants;
import com.example.proto.common.common.Common;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-7 下午9:16
 **/
@Slf4j
@ChannelHandler.Sharable
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Common.Msg message = (Common.Msg) msg;
        if (message.getHead().getMsgType().equals(Common.MsgType.HEART_BEAT)) {
            log.info("received heart beat msgBody from channelID:{}", ctx.channel().id());
            ctx.writeAndFlush(Constants.PONG);
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}
