package com.example.push.common;

import com.example.common.kafka.KafkaProducerUtil;
import com.example.common.util.ExecutorFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-14 下午5:00
 **/
@Slf4j
public class PushExecutor {

    private static final int threadSize = Runtime.getRuntime().availableProcessors();;

    /**
     * 线程池
     */
    private static final ExecutorService EXECUTOR_SERVICE = ExecutorFactory.fixedExecutors(threadSize);

    public static void execute(String topic, String json) {
        EXECUTOR_SERVICE.submit(new Runnable() {
            @Override
            public void run() {
                KafkaProducerUtil.sendSingle(topic, json, false);
                log.info("send to kafka successfully");
            }
        });
    }
}
