package com.example.netty.fake_aio;

import com.example.netty.common.Constants;
import com.example.netty.timeserver_bio.TimeServerHandle;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author kuro
 * @version V1.0
 * @date 20-2-26 下午9:39
 **/
public class FakeAioTimeServer {

    public static void main(String[] args) throws IOException {
        int port = Constants.DEFAULT_PORT;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException nf) {
                port = Constants.DEFAULT_PORT;
            }
        }
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("The time server is start in port : " + port);
            Socket socket = null;
            TimeServerHandlerExecutePool singleExecutor =
                    new TimeServerHandlerExecutePool(50, 10000);
            while (true) {
                // 没有请求进来，就阻塞在server.accept()方法里
                socket = server.accept();
                TimeServerHandle runnable = new TimeServerHandle(socket);
                Thread thread = new Thread(runnable);
                singleExecutor.execute(thread);
            }
        } finally {
            if (server != null) {
                System.out.println("The time server close");
                server.close();
                server = null;
            }
        }
    }
}