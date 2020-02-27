package com.example.netty.nio;

import com.example.netty.common.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author kuro
 * @version V1.0
 * @date 20-2-27 下午4:19
 **/
public class TimeServerNio {

    /**
     * NIO实现的TimeServer
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
        MultiplexerTimeServer multiplexerTimeServer =
                new MultiplexerTimeServer(port);
        new Thread(multiplexerTimeServer, "NIO-MultiplexerTimeServer-001").start();
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(100);
                multiplexerTimeServer.stop();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}