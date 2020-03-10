package com.example.common.util;

import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.Properties;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-10 下午5:44
 **/
public class KafkaProducerUtil {

    public static void main(String[] args) {
        Properties kafkaProps = new Properties();
        kafkaProps.put("bootstrap.servers", "broker1:port1, broker2:port2");
        kafkaProps.put("key.serializer", "org.apache.kafka.common.StringSerializer");
        kafkaProps.put("value.serializer", "org.apache.kafka.common.StringSerializer");
        KafkaProducer producer = new KafkaProducer<String, String>(kafkaProps);
    }
}
