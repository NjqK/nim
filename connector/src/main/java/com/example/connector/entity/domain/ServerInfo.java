package com.example.connector.entity.domain;

import lombok.Data;

/**
 * @author kuro
 * @version V1.0
 * @date 2020-04-27 4:26 PM
 **/
@Data
public class ServerInfo {
    private int weight;
    private int userCount;

    public ServerInfo(int weight, int userCount) {
        this.weight = weight;
        this.userCount = userCount;
    }
}
