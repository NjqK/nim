package com.example.connector.dao.manager.impl;

import com.example.api.inner.inner.ConnectorService;
import com.example.common.kafka.ReceiveMessageCallback;
import com.example.connector.common.ConnectorThreadFactory;
import com.example.connector.common.RedisKeyUtil;
import com.example.connector.common.SpringUtil;
import com.example.connector.dao.manager.SessionManager;
import com.example.connector.netty.NettyServerManager;
import com.example.connector.task.RecoverServerTask;
import com.example.connector.task.ReleaseConnectionsTask;
import com.example.proto.common.common.Common;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.zookeeper.server.quorum.ReadOnlyRequestProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-10 下午7:55
 **/
@Slf4j
public class ConnectorProcessor implements ReceiveMessageCallback<String, String> {

    /**
     * 发送消息用的
     */
    private NettyServerManager nettyServerManager;

    /**
     * session manager, 管理uid和channel
     */
    private SessionManager sessionManager;

    @Autowired
    public ConnectorProcessor(NettyServerManager nettyServerManager, SessionManager sessionManager) {
        this.nettyServerManager = nettyServerManager;
        this.sessionManager = sessionManager;
    }

    /**
     * 消费消息
     *
     * @param consumerRecords
     * @return
     */
    @Override
    public boolean processRecords(ConsumerRecords<String, String> consumerRecords) {

        for (ConsumerRecord<String, String> record : consumerRecords) {
            log.info("<=== Start process a batch push task got from kafka, batch size:{} ===>", consumerRecords.count());
            log.info("Got push task from kafka, topic:{}, partition:{}, offset:{}, key:{}, value:{}", record.topic(),
                    record.partition(), record.offset(), record.key(), record.value());
            Common.Msg.Builder msg = Common.Msg.newBuilder();
            try {
                JsonFormat.parser().merge(record.value(), msg);
            } catch (InvalidProtocolBufferException e) {
                log.error("Can not convert {} to PushRequest object, error message: {}", record.value(), e.getMessage());
                continue;
            }
            try {
                if (msg.getHead().getMsgType().equals(Common.MsgType.CHANGE_SERVER)) {
                    String content = msg.getBody().getContent();
                    if (StringUtils.isNotEmpty(content)) {
                        if (RedisKeyUtil.getApplicationRedisKey().equals(content)) {
                            // 创建任务去作
                            log.info("ConnectorProcessor, releasing===");
                            ConnectorThreadFactory.addJob(new ReleaseConnectionsTask(sessionManager));
                        }
                    } else {
                        log.error("ConnectorProcessor, change server cmd is faulty");
                    }
                    return true;
                }
                if (msg.getHead().getMsgType().equals(Common.MsgType.RECOVER_SERVER)) {
                    String content = msg.getBody().getContent();
                    if (StringUtils.isNotEmpty(content)) {
                        if (RedisKeyUtil.getApplicationRedisKey().equals(content)) {
                            // 恢复服务
                            log.info("ConnectorProcessor, recovering===");
                            ConnectorThreadFactory.addJob(new RecoverServerTask());
                        }
                    } else {
                        log.error("ConnectorProcessor, recover server cmd is faulty");
                    }
                    return true;
                }
                // 具体长连接发送逻辑
                String uid = String.valueOf(msg.getHead().getToId());
                log.info("start to send msg, uid:{}", uid);
                if (sessionManager.isOnline(uid)) {
                    // online
                    Channel channel = sessionManager.getChannel(uid);
                    if (!nettyServerManager.sendMsg(channel, msg.build())) {
                        log.error("sending msg is failed, netty service maybe unusable.");
                    }
                } else {
                    log.error("用户uid:{}不在线或者不在这个节点...", uid);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        log.info("<=== End process a batch im channel push task got from kafka, batch size:{} ===>", consumerRecords.count());
        return true;
    }
}
