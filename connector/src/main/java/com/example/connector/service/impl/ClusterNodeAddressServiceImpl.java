package com.example.connector.service.impl;

import com.example.api.inner.inner.ConnectorService;
import com.example.common.CommonConstants;
import com.example.connector.dao.manager.ClusterNodeManager;
import com.example.connector.entity.cluster.ClusterNode;
import com.example.proto.common.common.Common;
import com.example.proto.inner.inner.Inner;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Service(version = "1.0.0")
public class ClusterNodeAddressServiceImpl implements ConnectorService {

    @Autowired
    private ClusterNodeManager clusterNodeManager;

    @Override
    public Inner.GetNodeAddresssResp getNodeAddress(Inner.GetNodeAddresssReq req) {
        log.info("ClusterNodeAddressServiceImpl getNodeAddress, req:{}", req);
        Inner.GetNodeAddresssResp.Builder builder = Inner.GetNodeAddresssResp.newBuilder();
        try {
            ClusterNode localNode = clusterNodeManager.getLocalNode();
            if (localNode == null) {
                return builder.setRet(
                        Common.ErrorMsg.newBuilder()
                                .setErrorCode(Common.ErrCode.FAIL)
                                .setMsg("Connector service is unavailable.")
                                .build())
                        .build();
            }
            Inner.GetNodeAddresssResp resp = builder.setHost(localNode.getIp())
                    .setPort(localNode.getPort())
                    .build();
            log.info("ClusterNodeAddressServiceImpl getNodeAddress, resp:{}", resp);
            return resp;
        } catch (Exception e) {
            log.info("ClusterNodeAddressServiceImpl getNodeAddress occur error: {}", e);
            return builder.setRet(CommonConstants.FAIL).build();
        }
    }
}