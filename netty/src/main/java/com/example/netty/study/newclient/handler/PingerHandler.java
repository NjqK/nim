package com.example.netty.study.newclient.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.example.netty.study.common.Constants.PING;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-7 下午9:37
 **/
@Slf4j
public class PingerHandler extends ChannelInboundHandlerAdapter {

    private Random random = new Random();
    // TODO 把随机数放到别的类里，统一管理
    private int baseRandom = 10;

    private Channel channel;
    private ScheduledFuture future;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 保存下这个channel，以后fa心跳
        this.channel = ctx.channel();
        log.error("active");
        future = ping(channel);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (future != null) {
            future.cancel(true);
            log.info("清除Ping的定时任务...");
        }
    }

    /**
     * TODO 封装Ping间隔
     */
    private int getPingInterval() {
        return Math.max(1, random.nextInt(baseRandom));
    }

    /**
     * 发送心跳
     * @param channel
     */
    private ScheduledFuture ping(Channel channel) {
        // get num between [0,baseRandom)
        // TODO 配置ping的时间
        int interval = getPingInterval();
        log.info("sending rate: {}s/per", interval);
        ScheduledFuture<?> future = channel.eventLoop().scheduleAtFixedRate(() -> {
            if (channel.isActive()) {
                // 判断channel是否还连着
                log.info("sending heart beat to the server...");
                // TODO 移动到Constants
                channel.writeAndFlush(PING);
            }
        }, 0, interval, TimeUnit.SECONDS);
        return future;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 当Channel已经断开的情况下, 仍然发送数据, 会抛异常, 该方法会被调用.
        cause.printStackTrace();
        ctx.close();
    }
}
