package com.example.push.service.impl;

import com.example.api.inner.inner.PushService;
import com.example.common.CommonConstants;
import com.example.common.kafka.KafkaProducerUtil;
import com.example.common.redis.JedisUtil;
import com.example.common.util.ListUtil;
import com.example.proto.common.common.Common;
import com.example.proto.inner.inner.Inner;
import com.example.push.common.PushExecutor;
import com.google.protobuf.util.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-10 上午11:57
 **/
@Slf4j
@Service(version = "1.0.0")
public class PushServiceImpl implements PushService {

    @Override
    public Inner.BatchRouteMsgResp batchRouteMsg(Inner.BatchRouteMsgReq req) {
        log.info("Inner.BatchRouteMsgResp batchRouteMsg, req:{}", req);
        Inner.BatchRouteMsgResp.Builder builder = Inner.BatchRouteMsgResp.newBuilder();
        try {
            List<Inner.GuidUidBinder> toUidList = req.getToUidList();
            Inner.BatchRouteMsgResp resp = builder.setRet(CommonConstants.SUCCESS).build();
            if (ListUtil.isEmpty(toUidList)) {
                log.error("Inner.BatchRouteMsgResp batchRouteMsg, toUidList is empty.");
                return resp;
            }
            Common.Msg.Builder msgBuilder = req.getMsg().toBuilder();
            Common.Head.Builder headBuilder = req.getMsg().getHead().toBuilder();
            for (Inner.GuidUidBinder binder : toUidList) {
                String uid = String.valueOf(binder.getUid());
                String nettyNodeInfo = JedisUtil.hget(CommonConstants.USERS_REDIS_KEY, uid);
                if (nettyNodeInfo == null) {
                    log.error("Inner.BatchRouteMsgResp batchRouteMsg 用户uid:{}不在线.", uid);
                    continue;
                }
                headBuilder.setToId(binder.getUid());
                headBuilder.setMsgId(binder.getGuid());
                msgBuilder.setHead(headBuilder.build());
                String msgJson = JsonFormat.printer().print(msgBuilder);
                log.info("msg that will be send to kafka soon:{}", msgJson);
                // 可能会阻塞业务，放到线程池
                PushExecutor.execute(CommonConstants.CONNECTOR_KAFKA_TOPIC, msgJson);
            }
            log.info("Inner.BatchRouteMsgResp batchRouteMsg, resp:{}", resp);
            return resp;
        } catch (Exception e) {
            log.error("Inner.BatchRouteMsgResp batchRouteMsg, occur error:{}", e);
            return builder.setRet(CommonConstants.FAIL).build();
        }
    }

    @Override
    public Inner.RouteMsgResp routeMsg(Inner.RouteMsgReq req) {
        log.info("Inner.RouteMsgResp routeMsg, req:{}", req);
        Inner.RouteMsgResp.Builder builder = Inner.RouteMsgResp.newBuilder();
        try {
            if (req.getType().equals(Inner.RouteType.CMD)) {
                switch (req.getMsg().getHead().getMsgType()) {
                    case CHANGE_SERVER:
                    case RECOVER_SERVER:
                        String msgJson = JsonFormat.printer().print(req.getMsg());
                        KafkaProducerUtil.sendSingle(CommonConstants.CONNECTOR_KAFKA_TOPIC, msgJson, true);
                        break;
                    default:
                        log.error("UNKNOWN CMD MSG TYPE!");
                        break;
                }
                return builder.setRet(CommonConstants.SUCCESS).build();
            }
            // 找到netty，放到对应的kafka节点
            String nettyNodeInfo = JedisUtil.hget(CommonConstants.USERS_REDIS_KEY, req.getToUid());
            if (nettyNodeInfo == null) {
                log.error("用户不在线.");
                return builder.setRet(CommonConstants.FAIL).build();
            }
            // 把消息放到对应的kafka
            String msgJson = JsonFormat.printer().print(req.getMsg());
            KafkaProducerUtil.sendSingle(CommonConstants.CONNECTOR_KAFKA_TOPIC, msgJson, true);
            Inner.RouteMsgResp resp = builder.setRet(CommonConstants.SUCCESS).build();
            log.info("Inner.RouteMsgResp routeMsg, resp:{}", resp);
            return resp;
        } catch (Exception e) {
            log.error("Inner.RouteMsgResp routeMsg, occur error:{}", e);
            return builder.setRet(CommonConstants.FAIL).build();
        }
    }
}
