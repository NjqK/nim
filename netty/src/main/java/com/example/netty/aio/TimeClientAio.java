package com.example.netty.aio;

import com.example.netty.common.Constants;
import com.example.netty.timeserver_bio.TimeClientHandle;

/**
 * @author kuro
 * @version V1.0
 * @date 20-2-27 下午6:24
 **/
public class TimeClientAio {

    public static void main(String[] args) {
        int port = Constants.DEFAULT_PORT;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException nf) {
                port = Constants.DEFAULT_PORT;
            }
        }
        new Thread(new AsyncTimeClientHandler(Constants.DEFAULT_HOST, port),
                "AIO-AsyncTimeClientHandler-001").start();
    }
}