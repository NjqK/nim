package com.example.connector.task;

import com.alibaba.fastjson.JSON;
import com.example.common.CommonConstants;
import com.example.common.redis.JedisUtil;
import com.example.common.util.ListUtil;
import com.example.common.zk.ZkUtil;
import com.example.connector.common.DubboRouterUtil;
import com.example.connector.common.RedisKeyUtil;
import com.example.connector.dao.manager.SessionManager;
import com.example.connector.netty.NettyServerManager;
import com.example.proto.common.common.Common;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kuro
 * @version V1.0
 * @date 2020-04-27 1:16 PM
 **/
@Slf4j
public class ReleaseConnectionsTask implements Runnable {

    private SessionManager sessionManager;

    public ReleaseConnectionsTask(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void run() {
        log.info("removeConnections===start");
        try {
            List<String> allUid = new ArrayList<>(sessionManager.getAllUid());
            NettyServerManager instance = NettyServerManager.getInstance();
            // 删除key，防止别的节点的转移任务到这来
            String applicationRedisKey = RedisKeyUtil.getApplicationRedisKey();
            if (JedisUtil.hexists(CommonConstants.CONNECTOR_REDIS_KEY, applicationRedisKey)) {
                JedisUtil.hdel(CommonConstants.CONNECTOR_REDIS_KEY, applicationRedisKey);
            } else {
                return;
            }
            // 更改路由规则，防止进来新的连接请求
            blockService(applicationRedisKey);
            int uidCount = allUid.size();
            if (uidCount == 0) {
                log.info("no user on this node");
                return;
            }
            Map<String, String> allServers = JedisUtil.hgetall(CommonConstants.CONNECTOR_REDIS_KEY);
            List<String> child = ZkUtil.getChild(CommonConstants.CONNECTOR_ZK_BASE_PATH);
            log.info("可用服务节点:{}", JSON.toJSONString(child, true));
            Map<String, Integer> allAvailableServers = new HashMap<>(16);
            if (allServers == null || allServers.size() == 0 || ListUtil.isEmpty(child)) {
                log.error("ConnectorServiceImpl removeConnections, no available node");
                return;
            }
            int current = 0;
            int totalWeight = 0;
            for (String serverInfo : child) {
                if (!applicationRedisKey.equals(serverInfo)) {
                    String weight = allServers.get(serverInfo);
                    if (StringUtils.isNotEmpty(weight)) {
                        Integer v = Integer.valueOf(weight);
                        allAvailableServers.put(serverInfo, v);
                        totalWeight += v;
                    }
                }
            }
            if (totalWeight == 0) {
                log.error("ConnectorServiceImpl removeConnections total weight is 0");
                return;
            }
            log.info("allAvailableServers:{}", JSON.toJSONString(allAvailableServers, true));
            // assign job 分配任务
            Common.Head header = Common.Head.newBuilder()
                    .setMsgType(Common.MsgType.CHANGE_SERVER)
                    .setMsgContentType(Common.MsgContentType.TEXT)
                    .build();
            if (uidCount >= totalWeight) {
                while (current < uidCount) {
                    for (String serverInfo : allAvailableServers.keySet()) {
                        Common.Body body = Common.Body.newBuilder()
                                .setContent(serverInfo)
                                .build();
                        Common.Msg changeServerMsg = Common.Msg.newBuilder()
                                .setHead(header)
                                .setBody(body)
                                .build();
                        int weight = allAvailableServers.get(serverInfo);
                        for (int i = 0; i < weight; i++) {
                            instance.sendMsg(sessionManager.getChannel(allUid.get(current)), changeServerMsg);
                            current++;
                        }
                    }
                }
            } else {
                while (current < uidCount) {
                    for (String serverInfo : allAvailableServers.keySet()) {
                        Common.Body body = Common.Body.newBuilder()
                                .setContent(serverInfo)
                                .build();
                        Common.Msg changeServerMsg = Common.Msg.newBuilder()
                                .setHead(header)
                                .setBody(body)
                                .build();
                        int weight = allAvailableServers.get(serverInfo);
                        int proportion = Math.max((uidCount * weight) / totalWeight, 1);
                        for (int i = 0; i < proportion; i++) {
                            instance.sendMsg(sessionManager.getChannel(allUid.get(current)), changeServerMsg);
                            current++;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.info("removeConnections occur error: {}", e);
        }
    }

    /**
     * 修改route规则，屏蔽这个ip的这个服务，防止有新的连接进入
     * @param applicationRedisKey String
     */
    private void blockService(String applicationRedisKey) {
        DubboRouterUtil dubboRouterUtil = DubboRouterUtil.getINSTANCE();
        String ip = RedisKeyUtil.getIp(applicationRedisKey);
        String serviceName = "com.example.api.inner.inner.ConnectorService";
        String serviceVersion = "1.0.0";
        String routeRule = "host != " + ip;
        dubboRouterUtil.addRouteRule(serviceName, serviceVersion, routeRule);
    }
}
