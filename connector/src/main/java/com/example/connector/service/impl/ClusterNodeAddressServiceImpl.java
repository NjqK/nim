package com.example.connector.service.impl;

import com.example.api.inner.inner.ClusterNodeAddressService;
import com.example.connector.entity.cluster.ClusterNode;
import com.example.proto.inner.inner.Inner;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.NetUtils;
import org.apache.dubbo.config.annotation.Service;

@Slf4j
@Service(version = "1.0.0")
public class ClusterNodeAddressServiceImpl implements ClusterNodeAddressService {

    private ClusterNode localNode = null;

    private synchronized ClusterNode getLocalNode() {
        if (localNode == null) {
            String localHost = NetUtils.getLocalHost();
            localNode = new ClusterNode(localHost, "88");
        }
        return localNode;
    }

    @Override
    public Inner.GetNodeAddresssResp getNodeAddress(Inner.GetNodeAddresssReq getNodeAddressReq) {
        log.info("ClusterNodeAddressServiceImpl getNodeAddress, req:{}", getNodeAddressReq);
        ClusterNode localNode = getLocalNode();
        Inner.GetNodeAddresssResp resp = Inner.GetNodeAddresssResp.newBuilder()
                .setHost(localNode.getIp())
                .setPort(localNode.getPort())
                .build();
        log.info("ClusterNodeAddressServiceImpl getNodeAddress, resp:{}", resp);
        return resp;
    }
}