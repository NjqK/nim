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

    public KeyManager() {
        SERVER_RSA_PRIVATE_KEY = getServerRsaPrivateKey();
    }

    private static String getServerRsaPrivateKey() {
        try (FileReader reader = new FileReader(
                new ClassPathResource("secure/rsa/private_key.txt").getFile());
             BufferedReader bReader = new BufferedReader(reader);) {
            StringBuilder sb = new StringBuilder();
            String s = "";
            while ((s = bReader.readLine()) != null) {
                sb.append(s + "\n");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("读取服务器密钥失败");
        }
    }
}
