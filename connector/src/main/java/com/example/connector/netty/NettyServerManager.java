package com.example.connector.netty;

import com.example.common.util.ExecutorFactory;
import com.example.connector.common.ConnectorThreadFactory;
import com.example.connector.entity.domain.ClusterNode;
import com.example.connector.netty.listener.SendMsgListener;
import com.example.proto.common.common.Common;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-7 下午1:41
 **/
@Slf4j
public class NettyServerManager {

    /**
     * false还没初始化好
     */
    private boolean init = false;

    public synchronized void init(ClusterNode localNode) {
        if (!init && localNode != null) {
            ExecutorService executorService = ExecutorFactory.singleExecutor("NettyLauncherThread");
            NettyLauncher nettyLauncherWork = new NettyLauncher(localNode);
            ConnectorThreadFactory.addJob(nettyLauncherWork);
            while (!nettyLauncherWork.isStart()) {
                // wait
            }
            init = true;
            log.info("Netty初始化完成");
        } else {
            log.info("Netty初始化失败");
        }
    }

    /**
     * 发送消息
     *
     * @param channel
     * @param msg
     * @return
     */
    public boolean sendMsg(Channel channel, Common.Msg msg) {
        if (canOpt()) {
            if (channel == null || msg == null) {
                log.error("发送消息失败，channel或者msg为空.");
                return false;
            }
            ChannelFuture channelFuture = channel.writeAndFlush(msg);
            channelFuture.addListener(new SendMsgListener(msg));
            return true;
        }
        return false;
    }

    private boolean canOpt() {
        return init;
    }

    private static final NettyServerManager INSTANCE = new NettyServerManager();

    public static NettyServerManager getInstance() {
        return INSTANCE;
    }

    private NettyServerManager() {

    }
}
