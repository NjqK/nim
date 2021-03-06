package com.example.connector.task;

import com.alibaba.fastjson.JSON;
import com.example.common.CommonConstants;
import com.example.common.ServiceStatusEnum;
import com.example.common.redis.JedisUtil;
import com.example.common.util.ListUtil;
import com.example.common.zk.ZkUtil;
import com.example.connector.common.Constants;
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
            String originalWeight = JedisUtil.hget(CommonConstants.CONNECTOR_REDIS_KEY, applicationRedisKey);
            if (JedisUtil.hexists(CommonConstants.CONNECTOR_REDIS_KEY, applicationRedisKey)) {
                JedisUtil.hdel(CommonConstants.CONNECTOR_REDIS_KEY, applicationRedisKey);
            } else {
                return;
            }
            JedisUtil.hset(RedisKeyUtil.getApplicationRedisKey(), "releasing", "1");
            // 更改路由规则，防止进来新的连接请求
            blockService(applicationRedisKey);
            JedisUtil.hset(RedisKeyUtil.getApplicationRedisKey(), "status",
                    String.valueOf(ServiceStatusEnum.OUT_OF_SERVICE.getStatus()));
            Map<String, String> allServers = JedisUtil.hgetall(CommonConstants.CONNECTOR_REDIS_KEY);
            List<String> child = ZkUtil.getChild(CommonConstants.CONNECTOR_ZK_BASE_PATH);
            log.info("可用服务节点:{}", JSON.toJSONString(child, true));
            Map<String, Integer> allAvailableServers = new HashMap<>(16);
            if (allServers == null || allServers.size() == 0 || ListUtil.isEmpty(child)) {
                log.error("ConnectorServiceImpl removeConnections, no available node");
                unlock(applicationRedisKey, originalWeight);
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
            if (allAvailableServers.size() == 0) {
                // 系统只有本服务，添加的路由规则无效
                unlock(applicationRedisKey, originalWeight);
                return;
            }
            if (totalWeight == 0) {
                log.error("ConnectorServiceImpl removeConnections total weight is 0");
                JedisUtil.hset(RedisKeyUtil.getApplicationRedisKey(), "releasing", "0");
                return;
            }
            log.info("allAvailableServers:{}", JSON.toJSONString(allAvailableServers, true));
            int uidCount = allUid.size();
            if (uidCount == 0) {
                log.info("no user on this node, job finished");
                JedisUtil.hset(RedisKeyUtil.getApplicationRedisKey(), "releasing", "0");
                return;
            }
            // assign job 分配任务
            Common.Head header = Common.Head.newBuilder()
                    .setMsgType(Common.MsgType.CHANGE_SERVER)
                    .setMsgContentType(Common.MsgContentType.TEXT)
                    .build();
            String uid = allUid.get(current);
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
                            if (current < uidCount) {
                                instance.sendMsg(sessionManager.getChannel(uid), changeServerMsg);
                                sessionManager.destroySession(uid);
                                current++;
                            }
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
                        double v = uidCount * ((weight + 0.0) / totalWeight);
                        int proportion = new Double(Math.max(v, 1)).intValue();
                        for (int i = 0; i < proportion; i++) {
                            if (current < uidCount) {
                                instance.sendMsg(sessionManager.getChannel(uid), changeServerMsg);
                                sessionManager.destroySession(uid);
                                current++;
                            }
                        }
                    }
                }
            }
            JedisUtil.hset(RedisKeyUtil.getApplicationRedisKey(), "releasing", "0");
        } catch (Exception e) {
            log.info("removeConnections occur error: {}", e);
        }
    }

    private void unlock(String applicationRedisKey, String originalWeight) {
        unlockService(applicationRedisKey);
        JedisUtil.hset(RedisKeyUtil.getApplicationRedisKey(), "status",
                String.valueOf(ServiceStatusEnum.IN_SERVICE.getStatus()));
        JedisUtil.hset(RedisKeyUtil.getApplicationRedisKey(), "releasing", "0");
        JedisUtil.hset(CommonConstants.CONNECTOR_REDIS_KEY, applicationRedisKey, originalWeight);
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

    private void unlockService(String applicationRedisKey) {
        DubboRouterUtil dubboRouterUtil = DubboRouterUtil.getINSTANCE();
        String ip = RedisKeyUtil.getIp(applicationRedisKey);
        String routeRule = "host != " + ip;
        dubboRouterUtil.deleteRouteRule(Constants.CONNECTOR_SERVICE_NAME, Constants.SERVICE_VERSION, routeRule);
    }
}
