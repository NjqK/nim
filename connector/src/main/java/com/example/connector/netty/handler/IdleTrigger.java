package com.example.connector.netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-10 下午2:59
 **/
@Slf4j
@ChannelHandler.Sharable
public class IdleTrigger extends ChannelInboundHandlerAdapter {

    /**
     * 服务端设置多少秒没收到客户端的消息，就关闭channel
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            log.error("idle triggered");
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                Channel channel = ctx.channel();
                log.info("Lost connection with the client which channel id is :{}, then close it.", channel.id());
                // 传到后面去执行channel关闭逻辑
                ctx.fireChannelInactive();
            } else {
                super.userEventTriggered(ctx, evt);
            }
        }
    }
}
