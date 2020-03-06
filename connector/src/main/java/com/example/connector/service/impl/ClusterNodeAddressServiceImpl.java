package com.example.connector.service.impl;

import com.example.api.inner.inner.ClusterNodeAddressService;
import com.example.connector.dao.manager.ClusterNodeManager;
import com.example.connector.entity.cluster.ClusterNode;
import com.example.proto.inner.inner.Inner;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Service(version = "1.0.0")
public class ClusterNodeAddressServiceImpl implements ClusterNodeAddressService {

    @Autowired
    private ClusterNodeManager clusterNodeManager;

    @Override
    public Inner.GetNodeAddresssResp getNodeAddress(Inner.GetNodeAddresssReq getNodeAddressReq) {
        log.info("ClusterNodeAddressServiceImpl getNodeAddress, req:{}", getNodeAddressReq);
        ClusterNode localNode = clusterNodeManager.getLocalNode();
        Inner.GetNodeAddresssResp resp = Inner.GetNodeAddresssResp.newBuilder()
                .setHost(localNode.getIp())
                .setPort(localNode.getPort())
                .build();
        log.info("ClusterNodeAddressServiceImpl getNodeAddress, resp:{}", resp);
        return resp;
    }
}