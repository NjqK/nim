package com.example.netty.study.newclient.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import static com.example.netty.study.common.Constants.PING;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-7 下午9:30
 **/
public class ClientIdleStateTrigger extends ChannelInboundHandlerAdapter {

    /**
     * 用于捕获{@link IdleState#WRITER_IDLE}事件（未在指定时间内向服务器发送数据），
     * 然后向服务端发送一个心跳包。
     * @param ctx ChannelHandlerContext
     * @param evt Object
     * @throws Exception e
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                // write heartbeat to server
                ctx.writeAndFlush(PING);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

}

