package com.example.common.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecords;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-10 下午6:41
 **/
public interface ReceiveMessageCallback<K, V> {
    boolean processRecords(ConsumerRecords<K, V> consumerRecords);
}