package com.example.connector.netty.handler;

import com.example.common.CommonConstants;
import com.example.common.util.JedisUtil;
import com.example.common.util.ListUtil;
import com.example.connector.common.RedisKeyUtil;
import com.example.connector.common.SpringUtil;
import com.example.connector.dao.manager.SessionManager;
import com.example.proto.common.common.Common;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 业务处理
 *
 * @author kuro
 * @version v1.0
 * @date 20-3-7 下午3:12
 **/
@Slf4j
public class BizHandler extends ChannelInboundHandlerAdapter {

    /**
     * session manager, 管理uid和channel
     */
    private SessionManager sessionManager = SpringUtil.getBean(SessionManager.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Common.Msg message = (Common.Msg) msg;
        log.info("BizHandler got msgBody:{}", message);
        if (message.getHead().getMsgType().equals(Common.MsgType.HAND_SHAKE)) {
            String uid = message.getHead().getExtendMap().get("uid");
            if (uid != null) {
                log.info("create redis session, uid:{}", uid);
                if (JedisUtil.hset(CommonConstants.USERS_REDIS_KEY, uid, RedisKeyUtil.getApplicationRedisKey()) > 0) {
                    if (sessionManager.createSession(uid, ctx.channel())) {
                        // 绑定uid
                        AttributeKey<String> key = AttributeKey.valueOf("uid");
                        ctx.channel().attr(key).set(uid);
                    }
                }
            }
        } else {
            ctx.writeAndFlush(defaultMsg());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        Attribute<String> attr = channel.attr(AttributeKey.<String>valueOf("uid"));
        String uid = attr.get();
        log.info("delete redis session, uid:{}", uid);
        JedisUtil.hdel(CommonConstants.USERS_REDIS_KEY, uid);
        if (!sessionManager.destroySession(uid)) {
            log.error("deleting session is failed, uid:{}", uid);
        }
        channel.close();
    }

    private Common.Msg defaultMsg() {
        Common.Msg.Builder builder = Common.Msg.newBuilder();
        Common.Head header = Common.Head.newBuilder()
                .setMsgType(Common.MsgType.SINGLE_CHAT)
                .setMsgContentType(Common.MsgContentType.TEXT)
                .setTimestamp(111L)
                .setStatusReport(1)
                .build();
        Common.Body body = Common.Body.newBuilder()
                .setContent("msgBody received")
                .build();
        builder.setHead(header);
        builder.setBody(body);
        return builder.build();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("error:{}", cause.getMessage());
        String uid = ctx.channel().attr(AttributeKey.<String>valueOf("uid")).get();
        JedisUtil.hdel(CommonConstants.USERS_REDIS_KEY, uid);
        sessionManager.destroySession(uid);
        ctx.close();
    }
}
