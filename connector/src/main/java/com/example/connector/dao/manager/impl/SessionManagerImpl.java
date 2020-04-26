package com.example.connector.dao.manager.impl;

import com.example.common.CommonConstants;
import com.example.common.redis.JedisUtil;
import com.example.connector.dao.manager.SessionManager;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-10 下午1:09
 **/
@Slf4j
@Component("SessionManager")
public class SessionManagerImpl implements SessionManager {

    /**
     * 删除session 每批的个数
     */
    private static final int BATCH_SIZE = 500;

    /**
     * Sessions of this connector service node
     */
    private Map<String, Channel> session = new ConcurrentHashMap<>(16);

    /**
     * 创建session是用的锁
     */
    private final Lock writeLock = new ReentrantLock();

    @Override
    public boolean destroySession(String uid) {
        log.info("destroySession, uid:{}", uid);
        if (uid == null) {
            log.error("destroySession, uid is null");
            return false;
        }
        session.remove(uid);
        return true;
    }

    @Override
    public boolean isOnline(String uid) {
        log.info("isOnline, uid:{}", uid);
        if (uid == null) {
            log.error("isOnline, uid is null");
            return false;
        }
        return session.get(uid) != null;
    }

    @Override
    public Channel getChannel(String uid) {
        log.info("getChannel, uid:{}", uid);
        if (uid == null) {
            log.error("getChannel, uid is null");
            return null;
        }
        return session.get(uid);
    }

    @Override
    public boolean createIfAbsent(String uid, Channel channel) {
        if (getChannel(uid) != null) {
            return false;
        }
        synchronized (writeLock) {
            if (getChannel(uid) != null) {
                return false;
            }
            session.put(uid, channel);
            return true;
        }
    }

    @Override
    public Channel updateSession(String uid, Channel channel) {
        Channel origin = session.get(uid);
        session.put(uid, channel);
        return origin;
    }

    @Override
    public void serverDown() {
        log.error("session:{}", session);
        int batchWorks = 0;
        int size = session.size();
        if (size == 0) {
            return;
        }
        String[] uids;
        if (size < BATCH_SIZE) {
            batchWorks = size;
        } else {
            batchWorks = 500;
        }
        uids = new String[batchWorks];
        int index = 0;
        for (String uid : session.keySet()) {
            uids[index % batchWorks] = uid;
            if (index == (batchWorks - 1)) {
                // send
                JedisUtil.hdel(CommonConstants.USERS_REDIS_KEY, uids);
                uids = new String[batchWorks];
            }
        }
    }

    @Override
    public Set<String> getAllUid() {
        return session.keySet();
    }
}
