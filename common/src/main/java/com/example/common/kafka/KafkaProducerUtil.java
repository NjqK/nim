package com.example.common.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-10 下午5:44
 **/
@Slf4j
public class KafkaProducerUtil<K, V> {

    private KafkaProducer<K, V> producer;
    private Callback callback;

    private static class Instance {
        private static KafkaProducerUtil instance = new KafkaProducerUtil();
    }

    public static void init(String hosts, String clientId, Callback callback) {

        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, hosts);
        Instance.instance.producer = new KafkaProducer<>(props);
        if (callback == null){
            Instance.instance.callback = new DefaultSendCallback();
        }
    }

    public static boolean sendSingle(String topic, Object messge, boolean isAsync){
        return Instance.instance.sendSingleMessage(topic, String.valueOf(System.currentTimeMillis()), messge, isAsync);
    }

    /**
     * Send single message to specified topic
     *
     * @param topic
     * @param key message key， used for kafka partition load balancing
     * @param value message value
     * @param isAsync true:send message asynchronously, false:send message synchronously
     */
    public boolean sendSingleMessage(String topic, K key, V value, boolean isAsync) {
        if (topic == null || value == null) {
            log.error("kafka topic or bytes can not be null");
            return false;
        }

        log.info("Start to send message to {}, async: {}, key:{}, value: {}", topic, isAsync, key, value);
        if (isAsync) {
            producer.send(new ProducerRecord<>(topic, key, value), callback);
        } else {
            try {
                RecordMetadata metaData = producer.send(new ProducerRecord<>(topic, key, value)).get();
                log.info("Store {} to {} success, partition: {}, offset: {}", key, metaData.topic(),
                        metaData.partition(), metaData.offset());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return false;
            }
        }
        return true;
    }

    /**
     * Close kafka producer
     */
    public static void close() {
        if (Instance.instance.producer != null) {
            Instance.instance.producer.close();
        }
    }
}
