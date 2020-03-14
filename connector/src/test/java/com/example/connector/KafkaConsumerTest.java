package com.example.connector;

import com.example.common.CommonConstants;
import com.example.common.kafka.KafkaConsumerUtil;
import com.example.connector.dao.manager.impl.ConnectorProcessor;
import com.example.connector.netty.NettyServerManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-11 下午3:36
 **/
public class KafkaConsumerTest {
    public static void main(String[] args) {
        String kafkaNodes = "127.0.0.1:9092";
        String kafkaGroup = "push_task_group2";
        List<String> kafkaTopics = new ArrayList<>();
        kafkaTopics.add(CommonConstants.CONNECTOR_KAFKA_TOPIC);
        KafkaConsumerUtil.init(kafkaNodes, kafkaGroup, kafkaTopics
                , new ConnectorProcessor(NettyServerManager.getInstance(), null), true);
    }
}
