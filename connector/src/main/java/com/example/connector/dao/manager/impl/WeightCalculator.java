package com.example.connector.dao.manager.impl;

import com.example.connector.entity.domain.WeightPolicy;
import com.example.connector.entity.domain.ServiceLoad;

/**
 * @author kuro
 * @version V1.0
 * @date 2020-04-26 8:18 PM
 **/
public class WeightCalculator {
    /**
     * 计算规则
     */
    private WeightPolicy policy;

    /**
     * 计算长连接服务权重
     * @param serviceLoad ServiceLoad
     * @return serviceWeight int
     */
    public int calculateServiceWeight(ServiceLoad serviceLoad) {
        return  0;
    }
}
