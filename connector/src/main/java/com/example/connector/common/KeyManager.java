package com.example.connector.common;

import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * @author kuro
 * @version V1.0
 * @date 2020-05-05 6:51 PM
 **/
public class KeyManager {
    /**
     * 服务端RSA 密钥
     */
    public static String SERVER_RSA_PRIVATE_KEY;

    public KeyManager(String privateKey) {
        SERVER_RSA_PRIVATE_KEY = privateKey;
    }
}
