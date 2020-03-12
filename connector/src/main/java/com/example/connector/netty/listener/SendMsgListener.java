package com.example.connector.netty.listener;

import com.example.common.CommonConstants;
import com.example.common.kafka.KafkaProducerUtil;
import com.example.proto.common.common.Common;
import com.google.protobuf.util.JsonFormat;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-11 下午2:34
 **/
@Slf4j
public class SendMsgListener implements ChannelFutureListener {

    private Common.Msg msg;

    public SendMsgListener(Common.Msg msg) {
        this.msg = msg;
    }

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        if (future.isSuccess()) {
            log.info("msg send successfully, guid:{}", msg.getHead().getMsgId());
        } else {
            log.error("msg sending is failed, guid:{}", msg.getHead().getMsgId());
            String msgJson = JsonFormat.printer().print(msg);
            // 放回kafka等待消费
            KafkaProducerUtil.sendSingle(CommonConstants.CONNECTOR_KAFKA_TOPIC, msgJson, true);
        }
    }
}
