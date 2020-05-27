package com.example.connector.dao.manager.impl;

import com.example.connector.dao.manager.ClusterNodeManager;
import com.example.connector.entity.domain.ClusterNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.NetUtils;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${n-server.port}")
    private String port;

    private volatile ClusterNode localNode = null;

    @Override
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
            //TODO 服务器不好开放随机端口，还是使用固定的
            int fixedPort = Integer.parseInt(port);
            //读取空闲的可用端口
            // ServerSocket serverSocket =  new ServerSocket(0);
            ServerSocket serverSocket =  new ServerSocket(fixedPort);
            serverSocket.close();
            String port = String.valueOf(serverSocket.getLocalPort());
            String localHost = NetUtils.getLocalHost();
            return new ClusterNode(localHost, port);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}