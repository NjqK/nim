package com.example.chat.dao.manager;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-12 下午5:53
 **/
public interface MsgReadDaoManager {
    /**
     * 返回用户收到的最大guid
     *
     * @param uid 用户id
     * @return
     */
    long getMaxGuid(long uid);
}
