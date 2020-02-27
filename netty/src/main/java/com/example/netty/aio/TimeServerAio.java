package com.example.netty.aio;

import com.example.netty.common.Constants;
import com.example.netty.nio.MultiplexerTimeServer;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author kuro
 * @version V1.0
 * @date 20-2-27 下午4:19
 **/
public class TimeServerAio {

    /**
     * AIO实现的TimeServer
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        int port = Constants.DEFAULT_PORT;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException nf) {
                port = Constants.DEFAULT_PORT;
            }
        }
        AsyncTimeServerHandler timeServer = new AsyncTimeServerHandler(port);
        new Thread(timeServer, "AIO-AsyncTimeSeverHandler-001").start();
    }
}