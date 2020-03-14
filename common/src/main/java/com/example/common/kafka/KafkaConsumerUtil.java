package com.example.common.kafka;

import com.example.common.util.ExecutorFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-10 下午6:14
 **/
@Slf4j
public class KafkaConsumerUtil<K, V> {

    /**
     * 监听kafka topic的线程
     */
    private ExecutorService executor = ExecutorFactory.singleExecutor("KafkaConsumerUtil-start");
    /**
     * 线程可见
     */
    private volatile boolean running = false;
    /**
     * kafka消费者订阅主题
     */
    private Collection<String> topics;
    /**
     * 自动提交偏移量 true自动 false手动
     */
    private boolean autoCommit = true;
    /**
     * 收到消息的回调
     */
    private ReceiveMessageCallback<K, V> receiveMessageCallback;
    /**
     * kafka consumer
     */
    private KafkaConsumer<K, V> consumer;

    /**
     * 单例
     */
    private static class Instance {
        private static KafkaConsumerUtil instance = new KafkaConsumerUtil();
    }

    public static void init(String hosts, String group, Collection<String> topics,
                            ReceiveMessageCallback receiveMessageCallback, boolean autoCommit) {
        Instance.instance.topics = topics;
        Instance.instance.receiveMessageCallback = receiveMessageCallback;
        Instance.instance.autoCommit = autoCommit;
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, hosts);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        //自动更新offset，虽然不推荐
        if (autoCommit) {
            properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        }
        Instance.instance.consumer = new KafkaConsumer<>(properties);
        Instance.instance.start();
    }

    public static void destory() {
        Instance.instance.close();
    }

    public static KafkaConsumerUtil getInstance() {
        return Instance.instance;
    }

    public boolean start() {
        if (topics == null) {
            log.error("Please set subscribe topics first");
            return false;
        }
        this.running = true;
        executor.execute(new KafkaConsumerThread());
        return running;
    }

    private void close() {
        this.running = false;
        if (consumer != null) {
            consumer.close();
        }
    }

    private class KafkaConsumerThread implements Runnable {
        @Override
        public void run() {
            consumer.subscribe(topics);
            while (running) {
                ConsumerRecords<K, V> consumerRecords = consumer.poll(Duration.ofMillis(200));
                if (consumerRecords.isEmpty()) {
                    continue;
                }
                boolean processed = receiveMessageCallback.processRecords(consumerRecords);
                //等消息处理完了再异步提交
                if (!autoCommit && processed) {
                    consumer.commitSync();
                }
            }
        }
    }
}
