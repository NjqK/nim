package com.example.connector;

import com.example.common.secure.aes.AESUtil;

/**
 * @author kuro
 * @version V1.0
 * @date 2020-05-05 5:14 PM
 **/
public class AESTest {
    public static void main(String[] args) throws Exception {
        String keys = AESUtil.createKeys();
        String str = "站在大明门前守卫的禁卫军，事先没有接到\n"
                + "有关的命令，但看到大批盛装的官员来临，也就\n"
                + "以为确系举行大典，因而未加询问。进大明门即\n"
                + "为皇城。文武百官看到端门午门之前气氛平静，\n"
                + "城楼上下也无朝会的迹象，既无几案，站队点名\n"
                + "的御史和御前侍卫“大汉将军”也不见踪影，不免\n"
                + "心中揣测，互相询问：所谓午朝是否讹传？";
        System.out.println(keys);
        String encrypt = AESUtil.aesEncrypt(str, keys);
        System.out.println(encrypt);
        String decrypt = AESUtil.aesDecrypt(encrypt, keys);
        System.out.println(decrypt);
    }
}
