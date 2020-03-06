package com.example.connector.entity.cluster;

import lombok.Data;

/**
 * @author kuro
 * @version V1.0
 * @date 3/4/20 9:58 PM
 **/
@Data
public class ClusterNode {
    private String ip;
    private String port;

    public ClusterNode(String ip, String port) {
        this.ip = ip;
        this.port = port;
    }
}