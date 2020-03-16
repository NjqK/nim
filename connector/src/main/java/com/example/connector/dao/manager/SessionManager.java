package com.example.connector.dao.manager;

import io.netty.channel.Channel;

/**
 * 管理用户id 和 channel
 * @author kuro
 * @version v1.0
 * @date 20-3-10 下午1:06
 **/
public interface SessionManager {

    /**
     * 删除用户的session
     * @param uid
     * @return
     */
    boolean destroySession(String uid);

    /**
     * 查询用户是否在线
     * @param uid
     * @return
     */
    boolean isOnline(String uid);

    /**
     * 获取用户对应的channel
     * @param uid
     * @return
     */
    Channel getChannel(String uid);

    /**
     * 如果session没有就创建，并返回true，否则返回false
     *
     * @param uid
     * @param channel
     * @return
     */
    boolean createIfAbsent(String uid, Channel channel);

    /**
     * 更新用户session，可能短线重连，channel变了
     * @param uid
     * @param channel
     * @return
     */
    Channel updateSession(String uid, Channel channel);

    /**
     * 服务下线删除所有redis上的session
     */
    void serverDown();
}
