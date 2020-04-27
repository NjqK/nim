package com.example.connector.service.impl;

import com.example.api.inner.inner.ConnectorService;
import com.example.common.CommonConstants;
import com.example.connector.dao.manager.ClusterNodeManager;
import com.example.connector.entity.domain.ClusterNode;
import com.example.proto.common.common.Common;
import com.example.proto.inner.inner.Inner;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Service(version = "1.0.0")
public class ConnectorServiceImpl implements ConnectorService {

    @Autowired
    private ClusterNodeManager clusterNodeManager;

//    @Autowired
//    private SessionManager sessionManager;
//
//    @Override
//    public Inner.RemoveConnectionsResp removeConnections(Inner.RemoveConnectionsReq req) {
//        log.info("ConnectorServiceImpl removeConnections, req:{}", req);
//        Inner.RemoveConnectionsResp.Builder builder = Inner.RemoveConnectionsResp.newBuilder();
//        try {
//            List<String> allUid = new ArrayList<>(sessionManager.getAllUid());
//            NettyServerManager instance = NettyServerManager.getInstance();
//            // 删除key，防止别的节点的转移任务到这来
//            if (JedisUtil.hexists(CommonConstants.CONNECTOR_REDIS_KEY, RedisKeyUtil.getApplicationRedisKey())) {
//                JedisUtil.hdel(CommonConstants.CONNECTOR_REDIS_KEY, RedisKeyUtil.getApplicationRedisKey());
//            } else {
//                return builder.setRet(CommonConstants.SUCCESS).build();
//            }
//            // TODO 降级这个服务，防止新的连接请求
//
//            Map<String, String> allServers = JedisUtil.hgetall(CommonConstants.CONNECTOR_REDIS_KEY);
//            Map<String, Integer> allAvailableServers = new HashMap<>(16);
//            if (allServers == null || allServers.size() == 0) {
//                log.error("ConnectorServiceImpl removeConnections, no available node");
//                return builder.setRet(Common.ErrorMsg.newBuilder()
//                        .setErrorCode(Common.ErrCode.FAIL)
//                        .setMsg("removeConnections fail")
//                        .build()).build();
//            }
//            int current = 0;
//            int uidCount = allUid.size();
//            int totalWeight = 0;
//            for (String serverInfo : allServers.keySet()) {
//                String weight = allServers.get(serverInfo);
//                if (StringUtils.isNotEmpty(weight)) {
//                    Integer v = Integer.valueOf(weight);
//                    allAvailableServers.put(serverInfo, v);
//                    totalWeight += v;
//                }
//            }
//            if (totalWeight == 0) {
//                log.error("ConnectorServiceImpl removeConnections total weight is 0");
//                return builder.setRet(Common.ErrorMsg.newBuilder()
//                        .setErrorCode(Common.ErrCode.FAIL)
//                        .setMsg("total weight is 0")
//                        .build()).build();
//            }
//            // assign job 分配任务
//            Common.Head header = Common.Head.newBuilder()
//                    .setMsgType(Common.MsgType.CHANGE_SERVER)
//                    .setMsgContentType(Common.MsgContentType.TEXT)
//                    .build();
//            if (uidCount >= totalWeight) {
//                while (current < uidCount) {
//                    for (String serverInfo : allAvailableServers.keySet()) {
//                        Common.Body body = Common.Body.newBuilder()
//                                .setContent(serverInfo)
//                                .build();
//                        Common.Msg changeServerMsg = Common.Msg.newBuilder()
//                                .setHead(header)
//                                .setBody(body)
//                                .build();
//                        int weight = allAvailableServers.get(serverInfo);
//                        for (int i = 0; i < weight; i++) {
//                            instance.sendMsg(sessionManager.getChannel(allUid.get(current)), changeServerMsg);
//                            current++;
//                        }
//                    }
//                }
//            } else {
//                while (current < uidCount) {
//                    for (String serverInfo : allAvailableServers.keySet()) {
//                        Common.Body body = Common.Body.newBuilder()
//                                .setContent(serverInfo)
//                                .build();
//                        Common.Msg changeServerMsg = Common.Msg.newBuilder()
//                                .setHead(header)
//                                .setBody(body)
//                                .build();
//                        int weight = allAvailableServers.get(serverInfo);
//                        int proportion = Math.max((uidCount * weight) / totalWeight, 1);
//                        for (int i = 0; i < proportion; i++) {
//                            instance.sendMsg(sessionManager.getChannel(allUid.get(current)), changeServerMsg);
//                            current++;
//                        }
//                    }
//                }
//            }
//            return builder.setRet(CommonConstants.SUCCESS).build();
//        } catch (Exception e) {
//            log.info("ConnectorServiceImpl removeConnections occur error: {}", e);
//            return builder.setRet(CommonConstants.FAIL).build();
//        }
//    }

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