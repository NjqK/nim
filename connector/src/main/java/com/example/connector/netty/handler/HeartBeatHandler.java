package com.example.connector.netty.handler;

import com.example.connector.common.Constants;
import com.example.proto.common.common.Common;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-7 下午9:16
 **/
@Slf4j
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Common.Msg message = (Common.Msg) msg;
        if (message.getHead().getMsgType().equals(Common.MsgType.HEART_BEAT)) {
            log.info("received heart beat msg from channelID:{}", ctx.channel().id());
            ctx.writeAndFlush(Constants.PONG);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    /**
     * 服务端设置多少秒没收到客户端的消息，就关闭channel
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent)evt;
            if (event.state()== IdleState.READER_IDLE){
                log.info("Lost connection with the client which channel id is :{}", ctx.channel().id());
                ctx.channel().close();
            }
        }else {
            // 如果不是超过ReadIdle的事件，传给别的感兴趣的handler
            super.userEventTriggered(ctx,evt);
        }
    }
}
