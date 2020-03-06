package com.example.connector.dao.manager.impl;

import com.example.connector.dao.manager.ClusterNodeManager;
import com.example.connector.entity.cluster.ClusterNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.NetUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author kuro
 * @version V1.0
 * @date 3/6/20 3:00 PM
 **/
@Slf4j
@Component
public class ClusterNodeManagerImpl implements ClusterNodeManager {

    private ClusterNode localNode = null;

    public ClusterNode getLocalNode() {
        if (localNode == null) {
            localNode = getAvailablePort();
            if (localNode == null) {
                log.error("Getting available port is failed");
            }
        }
        return localNode;
    }

    /**
     * 获得可用的端口
     * @return ClusterNode
     */
    private ClusterNode getAvailablePort() {
        try {
            //读取空闲的可用端口
            ServerSocket serverSocket =  new ServerSocket(0);
            String port = String.valueOf(serverSocket.getLocalPort());
            String localHost = NetUtils.getLocalHost();
            return new ClusterNode(localHost, port);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}