package com.example.netty.study.netty;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.concurrent.TimeUnit;

/**
 * @author kuro
 * @version V1.0
 * @date 3/4/20 8:59 PM
 **/
public class EventLoopTest {

    public static void main(String[] args) {
        EventLoop eventExecutors = new DefaultEventLoop();
        ScheduledFuture<?> scheduledFuture = eventExecutors.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("1s");
            }
        }, 0, 1, TimeUnit.SECONDS);
        // ... wait a moment
        // eventExecutors.shutdownGracefully();
        // jscheduledFuture.cancel(true);
    }
}