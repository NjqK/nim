package com.example.connector.common;

import com.example.connector.entity.domain.ClusterNode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-10 下午12:40
 **/
public class RedisKeyUtil {

    private static String applicationRedisKey = null;

    /**
     * 创建应用的rediskey
     * @param applicationName
     * @param localNode
     * @return
     */
    public static String createApplicationRedisKey(String applicationName, ClusterNode localNode) {
        if (applicationRedisKey == null && localNode != null) {
            applicationRedisKey = applicationName + "_" + localNode.getIp() + "_" + localNode.getPort();
        }
        return applicationRedisKey;
    }

    public static String getApplicationRedisKey() {
        return applicationRedisKey;
    }

    public static String getIp(String key) {
        if (StringUtils.isNotEmpty(key)) {
            return key.split("_")[1];
        }
        return null;
    }

    public static String getHost(String key) {
        if (StringUtils.isNotEmpty(key)) {
            return key.split("_")[2];
        }
        return null;
    }
}
