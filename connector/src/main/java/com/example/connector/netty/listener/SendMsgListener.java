package com.example.connector.netty.listener;

import com.example.proto.common.common.Common;
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

    private long guid;

    public SendMsgListener(long guid) {
        this.guid = guid;
    }

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        if (future.isSuccess()) {
            log.info("msg send successfully, guid:{}", guid);
        } else {
            log.error("msg sending is failed, guid:{}", guid);
        }
    }
}
