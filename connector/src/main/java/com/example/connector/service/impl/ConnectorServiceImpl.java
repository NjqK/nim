package com.example.connector.service.impl;

import com.example.api.inner.inner.ConnectorService;
import com.example.common.CommonConstants;
import com.example.common.redis.JedisUtil;
import com.example.connector.common.RedisKeyUtil;
import com.example.connector.dao.manager.ClusterNodeManager;
import com.example.connector.dao.manager.SessionManager;
import com.example.connector.entity.domain.ClusterNode;
import com.example.connector.netty.NettyServerManager;
import com.example.proto.common.common.Common;
import com.example.proto.inner.inner.Inner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Set;

@Slf4j
@Service(version = "1.0.0")
public class ConnectorServiceImpl implements ConnectorService {

    @Autowired
    private ClusterNodeManager clusterNodeManager;

    @Autowired
    private SessionManager sessionManager;

    @Override
    public Inner.RemoveConnectionsResp removeConnections(Inner.RemoveConnectionsReq req) {
        log.info("ConnectorServiceImpl removeConnections, req:{}", req);
        Inner.RemoveConnectionsResp.Builder builder = Inner.RemoveConnectionsResp.newBuilder();
        try {
            Set<String> allUid = sessionManager.getAllUid();
            NettyServerManager instance = NettyServerManager.getInstance();
            JedisUtil.hdel(CommonConstants.CONNECTOR_REDIS_KEY, RedisKeyUtil.getApplicationRedisKey());
            Map<String, String> allServers = JedisUtil.hgetall(CommonConstants.CONNECTOR_REDIS_KEY);
            if (allServers == null || allServers.size() == 0) {
                return builder.setRet(Common.ErrorMsg.newBuilder()
                        .setErrorCode(Common.ErrCode.FAIL)
                        .setMsg("removeConnections fail")
                        .build()).build();
            }
            int uidCount = allUid.size();
            int totalWeight = 0;
            for (String serverInfo : allServers.keySet()) {
                String weight = allServers.get(serverInfo);
                if (StringUtils.isNotEmpty(weight)) {
                    totalWeight += Integer.parseInt(weight);
                }
            }

            // TODO 分配任务
            return null;
        } catch (Exception e) {
            log.info("ConnectorServiceImpl removeConnections occur error: {}", e);
            return builder.setRet(CommonConstants.FAIL).build();
        }
    }

    @Override
    public Inner.GetNodeAddresssResp getNodeAddress(Inner.GetNodeAddresssReq req) {
        log.info("ConnectorServiceImpl getNodeAddress, req:{}", req);
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
                    .setRet(CommonConstants.SUCCESS)
                    .setPort(localNode.getPort())
                    .build();
            log.info("ConnectorServiceImpl getNodeAddress, resp:{}", resp);
            return resp;
        } catch (Exception e) {
            log.info("ConnectorServiceImpl getNodeAddress occur error: {}", e);
            return builder.setRet(CommonConstants.FAIL).build();
        }
    }
}