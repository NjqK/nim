package com.example.connector.task;

import com.example.common.CommonConstants;
import com.example.common.ServiceStatusEnum;
import com.example.common.redis.JedisUtil;
import com.example.connector.common.Constants;
import com.example.connector.common.DubboRouterUtil;
import com.example.connector.common.GetSystemInfoUtil;
import com.example.connector.common.RedisKeyUtil;
import com.example.connector.dao.manager.impl.WeightCalculator;
import com.example.connector.entity.domain.RatePolicy;
import com.example.connector.entity.domain.ServiceLoad;

/**
 * @author kuro
 * @version V1.0
 * @date 2020-04-28 1:46 PM
 **/
public class RecoverServerTask implements Runnable {

    @Override
    public void run() {
        WeightCalculator weightCalculator = new WeightCalculator(RatePolicy.DEFAULT);
        double processCpuLoad = GetSystemInfoUtil.getProcessCpuLoad();
        long freePhysicalMemorySize = GetSystemInfoUtil.getFreePhysicalMemorySize();
        ServiceLoad serviceLoad = new ServiceLoad(processCpuLoad, freePhysicalMemorySize);
        long weight = Math.max(weightCalculator.calculateServiceWeight(serviceLoad), 1L);
        String value = String.valueOf(weight);
        String applicationRedisKey = RedisKeyUtil.getApplicationRedisKey();
        JedisUtil.hset(CommonConstants.CONNECTOR_REDIS_KEY, applicationRedisKey, value);
        JedisUtil.hset(RedisKeyUtil.getApplicationRedisKey(), "status", String.valueOf(ServiceStatusEnum.IN_SERVICE.getStatus()));
        recoverServer(applicationRedisKey);
    }

    /**
     * 恢复服务
     * @param applicationRedisKey
     */
    private void recoverServer(String applicationRedisKey) {
        DubboRouterUtil dubboRouterUtil = DubboRouterUtil.getINSTANCE();
        String ip = RedisKeyUtil.getIp(applicationRedisKey);
        String routeRule = "host != " + ip;
        dubboRouterUtil.deleteRouteRule(Constants.CONNECTOR_SERVICE_NAME, Constants.SERVICE_VERSION, routeRule);
    }
}
