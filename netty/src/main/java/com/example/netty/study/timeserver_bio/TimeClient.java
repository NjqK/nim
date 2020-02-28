package com.example.netty.study.timeserver_bio;

import com.example.netty.study.common.Constants;

import java.io.*;
import java.net.Socket;

/**
 * @author kuro
 * @version V1.0
 * @date 20-2-26 下午8:27
 **/
public class TimeClient {

    public static void main(String[] args) throws InterruptedException {
        int port = Constants.DEFAULT_PORT;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException nf) {
                port = Constants.DEFAULT_PORT;
            }
        }
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            socket = new Socket("127.0.0.1", port);
            InputStream inputStream = socket.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            OutputStream outputStream = socket.getOutputStream();
            in = new BufferedReader(reader);
            out = new PrintWriter(outputStream, true);
            out.println(Constants.DEFAULT_COMMAND);
            System.out.println("Send order to server succeed");
            String resp = in.readLine();
            System.out.println("Now is : " + resp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
                out = null;
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                socket = null;
            }
        }
    }
}