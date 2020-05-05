package com.example.connector;

import com.example.common.secure.rsa.RSAUtils;
import com.example.connector.common.KeyManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

public class RSAUtilTest {
    // 简单测试____________
    public static void main(String[] args) throws Exception {
        System.out.println(KeyManager.SERVER_RSA_PRIVATE_KEY);
        //createRSAKey();
        test1();
    }

    public static void createRSAKey() throws IOException {
        Map<String, String> keyMap = RSAUtils.createKeys(1024);
        String publicKey = keyMap.get("publicKey");
        String privateKey = keyMap.get("privateKey");
        System.out.println("公钥: \n\r" + publicKey);
        System.out.println("私钥： \n\r" + privateKey);
        FileWriter publicWriter = new FileWriter(new File("public_key.txt"));
        publicWriter.write(publicKey);
        publicWriter.close();
        FileWriter privateKeyWriter = new FileWriter(new File("private_key.txt"));
        privateKeyWriter.write(privateKey);
        privateKeyWriter.close();
    }

    public static void test1() throws InvalidKeySpecException, NoSuchAlgorithmException {
        Map<String, String> keyMap = RSAUtils.createKeys(1024);
        String publicKey = keyMap.get("publicKey");
        String privateKey = keyMap.get("privateKey");
        System.out.println("公钥: \n\r" + publicKey);
        System.out.println("私钥： \n\r" + privateKey);

        System.out.println("公钥加密——私钥解密");
        String str = "站在大明门前守卫的禁卫军，事先没有接到\n"
                + "有关的命令，但看到大批盛装的官员来临，也就\n"
                + "以为确系举行大典，因而未加询问。进大明门即\n"
                + "为皇城。文武百官看到端门午门之前气氛平静，\n"
                + "城楼上下也无朝会的迹象，既无几案，站队点名\n"
                + "的御史和御前侍卫“大汉将军”也不见踪影，不免\n"
                + "心中揣测，互相询问：所谓午朝是否讹传？";
        System.out.println("\r明文：\r\n" + str);
        System.out.println("\r明文大小：\r\n" + str.getBytes().length);
        //传入明文和公钥加密,得到密文
        String encodedData = RSAUtils.publicEncrypt(str, RSAUtils.getPublicKey(publicKey));
        System.out.println("密文：\r\n" + encodedData);
        //传入密文和私钥,得到明文
        String decodedData = RSAUtils.privateDecrypt(encodedData, RSAUtils.getPrivateKey(privateKey));
        System.out.println("解密后文字: \r\n" + decodedData);
    }
}
