package com.example.connector.dao.manager.impl;

import com.example.connector.dao.manager.SessionManager;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-10 下午1:09
 **/
@Slf4j
@Component("SessionManager")
public class SessionManagerImpl implements SessionManager {

    /**
     * Session of this connector service node
     */
    private Map<String, Channel> session = new ConcurrentHashMap<>(16);

    @Override
    public boolean createSession(String uid, Channel channel) {
        log.info("createSession, uid:{}, channel:{}", uid, channel);
        if (uid == null || channel == null) {
            throw new RuntimeException("createSession, uid or channel is null");
        }
        session.putIfAbsent(uid, channel);
        return true;
    }

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
    public boolean updateSession(String uid, Channel channel) {
        log.info("updateSession, uid:{}, channel:{}", uid, channel);
        if (uid == null) {
            log.error("updateSession, uid is null");
            return false;
        }
        if (session.get(uid) != null) {
            session.put(uid, channel);
            return true;
        } else {
            log.error("updateSession, uid is null");
            return false;
        }
    }
}
