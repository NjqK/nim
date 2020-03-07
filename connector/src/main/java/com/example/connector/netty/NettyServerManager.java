package com.example.connector.netty;

import com.example.connector.common.ExecutorFactory;
import com.example.connector.entity.cluster.ClusterNode;
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
            executorService.submit(nettyLauncherWork);
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
     * method sample
     * @param userId
     * @return
     */
    public boolean sendMsg(String userId) {
        if (canOpt()) {
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
