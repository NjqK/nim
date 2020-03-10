package com.example.common.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-10 下午6:44
 **/
@Slf4j
public class DefaultSendCallback implements Callback {

    @Override
    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
        if (null != e) {
            log.error(e.getMessage(), e);
            return;
        }
        log.info("Store message to {} success, partition: {}, offset: {}", recordMetadata.topic(),
                recordMetadata.partition(), recordMetadata.offset());
    }
}