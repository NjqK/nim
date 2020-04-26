package com.example.connector.entity.domain;

import lombok.Getter;

/**
 * @author kuro
 * @version V1.0
 * @date 2020-04-26 12:16 PM
 **/
@Getter
public class ServiceLoad {
    /**
     * cpu负载
     */
    private final double cpuLoad;
    /**
     * 可用内存
     */
    private final int freeMem;

    public ServiceLoad(double cpuLoad, int freeMem) {
        this.cpuLoad = cpuLoad;
        this.freeMem = freeMem;
    }
}