package com.example.connector.entity.domain;

import lombok.Data;

/**
 * @author kuro
 * @version V1.0
 * @date 2020-05-05 7:08 PM
 **/
@Data
public class SecretKey {
    /**
     * 客户端AES密钥
     */
    private String clientAESKey;
    /**
     * 服务端AES密钥
     */
    private String ServerAESKey;

    public SecretKey() {
    }

    public SecretKey(String clientAESKey, String serverAESKey) {
        this.clientAESKey = clientAESKey;
        ServerAESKey = serverAESKey;
    }
}
