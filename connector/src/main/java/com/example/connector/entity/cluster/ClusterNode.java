package com.example.connector.entity.cluster;

import lombok.Data;
import lombok.ToString;

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

    @Override
    public String toString() {
        return "ip_" + port;
    }

    /**
     * 由toString返回的值生成
     * @return ClusterNode
     */
    public static ClusterNode getFromString(String s) {
        String[] ipAndPort = s.split("_");
        if (ipAndPort.length != 2) {
            return null;
        }
        return new ClusterNode(ipAndPort[0], ipAndPort[1]);
    }
}