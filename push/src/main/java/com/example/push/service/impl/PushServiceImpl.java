package com.example.push.service.impl;

import com.example.api.inner.inner.PushService;
import com.example.common.CommonConstants;
import com.example.common.kafka.KafkaProducerUtil;
import com.example.common.redis.JedisUtil;
import com.example.proto.inner.inner.Inner;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-10 上午11:57
 **/
@Slf4j
@Service(version = "1.0.0")
public class PushServiceImpl implements PushService {

    @Override
    public Inner.RouteMsgResp routeMsg(Inner.RouteMsgReq req) {
        log.info("Inner.RouteMsgResp routeMsg, req:{}", req);
        Inner.RouteMsgResp.Builder builder = Inner.RouteMsgResp.newBuilder();
        try {
            // TODO 找到netty，放到对应的kafka节点
            String nettyNodeInfo = JedisUtil.hget(CommonConstants.USERS_REDIS_KEY, req.getToUid());
            if (nettyNodeInfo == null) {
                log.error("用户不在线.");
                return builder.setRet(CommonConstants.FAIL).build();
            }
            // TODO 把消息放到对应的kafka
            KafkaProducerUtil.sendSingle(CommonConstants.CONNECTOR_KAFKA_TOPIC, req.getMsg(), true);
            Inner.RouteMsgResp resp = builder.setRet(CommonConstants.SUCCESS).build();
            log.info("Inner.RouteMsgResp routeMsg, resp:{}", resp);
            return resp;
        } catch (Exception e) {
            log.error("Inner.RouteMsgResp routeMsg, occur error:{}", e);
            return builder.setRet(CommonConstants.FAIL).build();
        }
    }
}
