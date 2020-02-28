package com.example.netty.study.nio.learn;

import com.example.netty.study.common.Constants;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author kuro
 * @version V1.0
 * @date 20-2-27 下午3:45
 **/
public class NioChannel {

    /**
     * Nio主要的channel
     * @param args
     */
    public static void main(String[] args) throws IOException {
        SelectableChannel selectableChannel;
        ServerSocketChannel acceptorServer;
        SocketChannel socketChannel;

        FileChannel fileChannel;

        acceptorServer = ServerSocketChannel.open();
        acceptorServer.socket().bind(
                new InetSocketAddress(InetAddress.getByName("IP"), Constants.DEFAULT_PORT));
        acceptorServer.configureBlocking(false);
        Selector selector = Selector.open();
        //new Thread(new ReactorTask()).start();
        //SelectionKey key = acceptorServer.register(selector, SelectionKey.OP_ACCEPT, ioHandler);
        int num = selector.select();
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey next = iterator.next();
            // deal with I/O event
        }
        SocketChannel channel = acceptorServer.accept();
        channel.configureBlocking(false);
        channel.socket().setReuseAddress(true);
    }
}