package com.example.netty.study.netty.listener;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-8 下午1:04
 **/
@Slf4j
public class ClientConnector {

    private Bootstrap bootstrap;
    /**
     * TODO 封装重连规则
     */
    private int retryTimes = 10;
    private int interval = 500;
    private TimeUnit unit = TimeUnit.MILLISECONDS;

    public ClientConnector(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    public ClientConnector(Bootstrap bootstrap, int retryTimes, int interval, TimeUnit unit) {
        this.bootstrap = bootstrap;
        this.retryTimes = retryTimes;
        this.interval = interval;
        this.unit = unit;
    }

    public ChannelFuture connect() throws InterruptedException {
        ChannelFuture f = null;
        while (retryTimes > 0) {
            try {
                f = bootstrap.connect().sync();
                if (f.isSuccess()) {
                    log.info("客户端连接成功...");
                    break;
                } else {
                    --retryTimes;
                    log.info("客户端连接失败，尝试重连，剩余次数:{}", retryTimes);
                }
            } catch (Exception e) {
                --retryTimes;
                log.error("客户端连接失败, error:{}", e.getMessage());
            }
            if (retryTimes == 0) {
                break;
            }
            unit.sleep(interval);
        }
        return f;
    }

    public void reConnect() {

    }
}
