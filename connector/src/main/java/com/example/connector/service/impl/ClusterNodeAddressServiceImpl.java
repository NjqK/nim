package com.example.connector.service.impl;

import com.example.api.inner.inner.ClusterNodeAddressService;
import com.example.connector.entity.cluster.ClusterNode;
import com.example.proto.inner.inner.Inner;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;

@Slf4j
@Service(version = "1.0.0", interfaceClass = com.example.api.inner.inner.ClusterNodeAddressService.class)
public class ClusterNodeAddressServiceImpl implements ClusterNodeAddressService {

    private ClusterNode localNode = null;

    private synchronized ClusterNode getLocalNode() {
        if (localNode == null) {
            String localHost = "127.0.0.1";
            localNode = new ClusterNode(localHost, "88");
        }
        return localNode;
    }

    @Override
    public Inner.GetNodeAddresssResp getNodeAddress(Inner.GetNodeAddresssReq getNodeAddresssReq) {
        log.info("ClusterNodeAddressServiceImpl getNodeAddress, req:{}", getNodeAddresssReq);
        ClusterNode localNode = getLocalNode();
        log.info("ClusterNodeAddressServiceImpl getNodeAddress, resp:{}", localNode);
        return Inner.GetNodeAddresssResp.newBuilder()
                .setHost(localNode.getIp())
                .setPort(localNode.getPort()).build();
    }
}