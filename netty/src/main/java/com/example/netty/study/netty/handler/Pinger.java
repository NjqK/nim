package com.example.netty.study.netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-7 下午9:37
 **/
@Slf4j
public class Pinger extends ChannelInboundHandlerAdapter {

    private Random random = new Random();
    private int baseRandom = 10;

    private Channel channel;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        // 保存下这个channel，以后法心跳
        this.channel = ctx.channel();
        ping(ctx.channel());
    }

    private int getPingInterval() {
        return Math.max(1, random.nextInt(baseRandom));
    }

    /**
     * 发送心跳
     * @param channel
     */
    private void ping(Channel channel) {
        // get num between [0,baseRandom)
        // TODO 配置ping的时间
        int interval = getPingInterval();
        log.info("interval:{}", interval);
        log.info("next heart beat will send after {}s.", interval);
        ScheduledFuture<?> future = channel.eventLoop().scheduleAtFixedRate(() -> {
            if (channel.isActive()) {
                // 判断channel是否还连着
                log.info("sending heart beat to the server...");
                // TODO 移动到Constants
                channel.writeAndFlush(ClientIdleStateTrigger.HEART_BEAT);
            } else {
                log.error("The connection had broken, cancel the task that will send a heart beat.");
                channel.closeFuture();
                throw new RuntimeException("Lost connection with server.");
            }
        }, 0, interval, TimeUnit.SECONDS);
//        future.addListener(new GenericFutureListener() {
//            @Override
//            public void operationComplete(Future future) throws Exception {
//                if (future.isSuccess()) {
//                    ping(channel);
//                }
//            }
//        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 当Channel已经断开的情况下, 仍然发送数据, 会抛异常, 该方法会被调用.
        cause.printStackTrace();
        ctx.close();
    }
}
