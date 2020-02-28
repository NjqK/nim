package com.example.netty.study.timeserver_bio;

import com.example.netty.study.common.Constants;

import java.io.*;
import java.net.Socket;
import java.util.Date;

/**
 * @author kuro
 * @version V1.0
 * @date 20-2-26 下午8:04
 **/
public class TimeServerHandle implements Runnable {

    private Socket socket;

    public TimeServerHandle(Socket socket) {
        System.out.println("Receive a socket");
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("Tread running");
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            InputStream inputStream = this.socket.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            OutputStream outputStream = this.socket.getOutputStream();
            in = new BufferedReader(reader);
            out = new PrintWriter(outputStream, true);
            String currentTime = null;
            String body = null;
            while (true) {
                body = in.readLine();
                if (body == null) {
                    break;
                }
                System.out.println("BODY: " + body);
                System.out.println("The time server receive order: " + body);
                currentTime = Constants.DEFAULT_COMMAND.equalsIgnoreCase(body)
                        ? new Date(System.currentTimeMillis()).toString()
                        : Constants.DEFAULT_RESP;
                out.println(currentTime);
                System.out.println(currentTime);
            }
        } catch (IOException e) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (out != null) {
                out.close();
            }
            if (this.socket != null) {
                try {
                    this.socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            System.out.println("catch exception: " + e);
        }
    }
}