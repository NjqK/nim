package com.example.netty.study.nio;

import com.example.netty.study.common.Constants;
import com.example.netty.study.timeserver_bio.TimeClientHandle;

/**
 * @author kuro
 * @version V1.0
 * @date 20-2-27 下午6:24
 **/
public class TimeClientNio {

    public static void main(String[] args) {
        int port = Constants.DEFAULT_PORT;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException nf) {
                port = Constants.DEFAULT_PORT;
            }
        }
        new Thread(new TimeClientHandle(Constants.DEFAULT_HOST, port),
                "TimeClientAio-001").start();
    }
}