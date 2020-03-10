package com.example.connector.dao.manager.impl;

import com.example.common.kafka.ReceiveMessageCallback;
import com.example.proto.common.common.Common;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-10 下午6:55
 **/
@Slf4j
public class ConnectorProcessor implements ReceiveMessageCallback<String, String> {

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
            Common.Msg.Builder builder = Common.Msg.newBuilder();
            try {
                JsonFormat.parser().merge(record.value(), builder);
            } catch (InvalidProtocolBufferException e) {
                log.error("Can not convert {} to PushRequest object, error message: {}", record.value(), e.getMessage());
                continue;
            }
            try {
                // TODO 具体长连接发送逻辑

            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        log.info("<=== End process a batch im channel push task got from kafka, batch size:{} ===>", consumerRecords.count());

        return true;
    }
}