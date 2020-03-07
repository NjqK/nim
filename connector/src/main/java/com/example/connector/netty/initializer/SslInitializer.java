package com.example.connector.netty.initializer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-7 下午6:45
 **/
public class SslInitializer extends ChannelInitializer<Channel> {

    private final SslContext sslContext;
    private final boolean startTls;


    public SslInitializer(SslContext sslContext, boolean startTls) {
        this.sslContext = sslContext;
        this.startTls = startTls;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        SSLEngine sslEngine = sslContext.newEngine(ch.alloc());
        ChannelPipeline pipeline = ch.pipeline();
        // add ssl handler
        pipeline.addFirst("ssl", new SslHandler(sslEngine, startTls));
    }
}
