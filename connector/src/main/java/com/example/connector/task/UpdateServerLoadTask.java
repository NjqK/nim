package com.example.connector.task;

import com.example.common.CommonConstants;
import com.example.common.redis.JedisUtil;
import com.example.connector.common.GetSystemInfoUtil;
import com.example.connector.common.RedisKeyUtil;
import com.example.connector.dao.manager.impl.WeightCalculator;
import com.example.connector.entity.domain.ServiceLoad;

/**
 * @author kuro
 * @version V1.0
 * @date 2020-04-27 1:16 PM
 **/
public class UpdateServerLoadTask implements Runnable {

    private WeightCalculator weightCalculator;

    public UpdateServerLoadTask(WeightCalculator weightCalculator) {
        this.weightCalculator = weightCalculator;
    }

    @Override
    public void run() {
        double processCpuLoad = GetSystemInfoUtil.getProcessCpuLoad();
        long freePhysicalMemorySize = GetSystemInfoUtil.getFreePhysicalMemorySize();
        ServiceLoad serviceLoad = new ServiceLoad(processCpuLoad, freePhysicalMemorySize);
        long weight = Math.max(weightCalculator.calculateServiceWeight(serviceLoad), 1L);
        String value = String.valueOf(weight);
        JedisUtil.hset(CommonConstants.CONNECTOR_REDIS_KEY, RedisKeyUtil.getApplicationRedisKey(), value);
        JedisUtil.hset(RedisKeyUtil.getApplicationRedisKey(), "weight", value);
    }
}
