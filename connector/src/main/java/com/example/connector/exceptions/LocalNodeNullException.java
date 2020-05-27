package com.example.connector.exceptions;

public class LocalNodeNullException extends RuntimeException {

    public LocalNodeNullException() {
        super("获取可用本地节点出错，可能是端口已经被占用！");
    }
}
