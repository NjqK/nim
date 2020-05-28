package com.example.connector.netty.handler;

import com.alibaba.fastjson.JSON;
import com.example.common.CommonConstants;
import com.example.common.redis.JedisUtil;
import com.example.common.secure.rsa.RSAUtils;
import com.example.common.util.ListUtil;
import com.example.connector.common.KeyManager;
import com.example.connector.common.RedisKeyUtil;
import com.example.connector.common.SpringUtil;
import com.example.connector.dao.manager.SessionManager;
import com.example.proto.common.common.Common;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务处理
 *
 * @author kuro
 * @version v1.0
 * @date 20-3-7 下午3:12
 **/
@Slf4j
@ChannelHandler.Sharable
public class BizHandler extends ChannelInboundHandlerAdapter {
    /**
     * kick message
     */
    private static final Common.Msg KICK_MSG = Common.Msg.newBuilder()
            .setHead(Common.Head.newBuilder()
                    .setMsgType(Common.MsgType.KICK)
                    .build())
            .build();
    /**
     * session manager, 管理uid和channel
     */
    private SessionManager sessionManager = SpringUtil.getBean(SessionManager.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        Common.Msg message = (Common.Msg) msg;
        log.info("BizHandler got msgBody:{}", message);
        if (Common.MsgType.HAND_SHAKE.equals(message.getHead().getMsgType())) {
            Map<String, String> extraHeader = getExtraHeader(message);
            log.info("extraHeader:{}", JSON.toJSONString(extraHeader, true));
            String uid = extraHeader.get("uid");
            if (!extraHeader.containsKey("clientAESKey")) {
                log.error("没有客户端密钥");
                ctx.writeAndFlush(KICK_MSG);
            }
            if (uid == null) {
                log.error("用户id空");
                ctx.writeAndFlush(KICK_MSG);
            }
            // 解密客户端密钥，并保存
            String clientAESKey = RSAUtils.privateDecrypt(extraHeader.get("clientAESKey"),
                    RSAUtils.getPrivateKey(KeyManager.SERVER_RSA_PRIVATE_KEY));
            // TODO 因为目前只有服务端发送消息给客户端，那么不需要把服务端的AES密钥也发送给客户端，如果以后有需求可以在这里改
            // 生成服务端AES密钥
            //String serverAESKey = AESUtil.createKeys();
            //SecretKey secretKeyDto = new SecretKey(clientAESKey, serverAESKey);
            //JedisUtil.hset(CommonConstants.CONNECTOR_SECRET_REDIS_KEY, uid, JSON.toJSONString(secretKeyDto));
            JedisUtil.hset(CommonConstants.CONNECTOR_SECRET_REDIS_KEY, uid, clientAESKey);
            log.info("creating user's session, uid:{}", uid);
            if (JedisUtil.hset(CommonConstants.USERS_REDIS_KEY, uid, RedisKeyUtil.getApplicationRedisKey()) >= 0) {
                Channel channel = ctx.channel();
                if (!sessionManager.createIfAbsent(uid, channel)) {
                    log.info("已经有别的Channel");
                    // 已经有别的了
                    Channel origin = sessionManager.updateSession(uid, channel);
                    origin.writeAndFlush(KICK_MSG);
                    origin.close();
                    // TODO there is a bug that origin.close() will trigger channelInactive
                    // TODO to delete new user's session
                }
                AttributeKey<String> key = AttributeKey.valueOf("uid");
                channel.attr(key).set(uid);
                JedisUtil.hincrby(RedisKeyUtil.getApplicationRedisKey(), "userCount", 1L);
            }
            // 使用客户端密钥加密服务端密钥，并返回给客户端
        }
    }

    /**
     * 获取握手消息携带的用户id
     *
     * @param message
     * @return
     */
    private Map<String, String> getExtraHeader(Common.Msg message) {
        Map<String, String> handShakeMap = new HashMap<>(4);
        List<Common.ExtraHeader> extendsList = message.getHead().getExtendsList();
        if (ListUtil.isEmpty(extendsList)) {
            log.error("握手的消息包有误.");
            return null;
        }
        for (Common.ExtraHeader extraHeader : extendsList) {
            handShakeMap.put(extraHeader.getKey(), extraHeader.getValue());
        }
        return handShakeMap;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        Attribute<String> attr = channel.attr(AttributeKey.<String>valueOf("uid"));
        String uid = attr.get();
        if (uid != null) {
            log.info("delete redis session, uid:{}", uid);
            Channel nowChannel = sessionManager.getChannel(uid);
            if (nowChannel != null && nowChannel.equals(channel)) {
                JedisUtil.hdel(CommonConstants.USERS_REDIS_KEY, uid);
                if (!sessionManager.destroySession(uid)) {
                    log.error("deleting session is failed, uid:{}", uid);
                }
            }
            JedisUtil.hincrby(RedisKeyUtil.getApplicationRedisKey(), "userCount", -1L);
        }
        channel.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("error:{}", cause.getStackTrace());
        String uid = ctx.channel().attr(AttributeKey.<String>valueOf("uid")).get();
        if (uid != null) {
            JedisUtil.hdel(CommonConstants.USERS_REDIS_KEY, uid);
            sessionManager.destroySession(uid);
            JedisUtil.hincrby(RedisKeyUtil.getApplicationRedisKey(), "userCount", -1L);
        }
        ctx.close();
    }
}
