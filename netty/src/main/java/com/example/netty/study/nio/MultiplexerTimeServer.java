package com.example.netty.study.nio;

import com.alibaba.fastjson.JSON;
import com.example.netty.study.common.Constants;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * @author kuro
 * @version V1.0
 * @date 20-2-27 下午4:23
 **/
public class MultiplexerTimeServer implements Runnable {

    private Selector selector;
    private ServerSocketChannel serverChannel;
    private volatile boolean stop;

    /**
     * 初始化多路复用器，绑定监听端口
     *
     * @param port
     */
    public MultiplexerTimeServer(int port) {
        try {
            selector = Selector.open();
            serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.socket().bind(new InetSocketAddress(port), 1024);
            // 监听SelectionKey.OP_ACCEPT操作位
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("The time server is start in port: " + port);
        } catch (IOException e) {
            // 初始化失败，例如端口被占用
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                // selector每1s唤醒一次
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key = null;
                while (it.hasNext()) {
                    key = it.next();
                    it.remove();
                    try {
                        handleInput(key);
                    } catch (Exception e) {
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 多路复用器关闭后，
        // 所有注册在上面的channel和pipe等资源都会被自动注册并关闭，
        // 所以不需要重复释放资源
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("服务器关闭...");
        }
    }

    private void  handleInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            // 处理新接入的请求消息
            if (key.isAcceptable()) {
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                SocketChannel sc = ssc.accept();
                // 设为异步非阻塞，还可以设置TCP接受和发送缓冲区的大小等
                sc.configureBlocking(false);
                // add the new connection to the selector
                sc.register(selector, SelectionKey.OP_READ);
                System.out.println("连接客户端成功，信息：\n" + JSON.toJSON(sc));
            }
            if (key.isReadable()) {
                SocketChannel sc = (SocketChannel) key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                if (readBytes > 0) {
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    System.out.println("客户端的请求: " + body);
                    String currentTime = Constants.DEFAULT_COMMAND.equalsIgnoreCase(body)
                            ? new Date(System.currentTimeMillis()).toString()
                            : Constants.DEFAULT_RESP;
                    doWrite(sc, currentTime);
                } else if (readBytes < 0) {
                    // 链路已经关闭，需要释放SocketChannel的资源
                    key.cancel();
                    sc.close();
                } else {
                    // 正常场景
                    System.out.println("读到了0字节，忽略");
                }
            }
        }

    }

    /**
     * 返回数据，向SocketChannel写入数据
     * @param channel SocketChannel
     * @param response String
     * @throws IOException
     */
    private void doWrite(SocketChannel channel, String response) throws IOException {
        if (response != null && response.trim().length() > 0) {
            byte[] bytes = response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            channel.write(writeBuffer);
            System.out.println("响应客户端， \n" + JSON.toJSON(channel));
        }
    }
}
