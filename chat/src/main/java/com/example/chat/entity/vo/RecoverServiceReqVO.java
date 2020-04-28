package com.example.chat.entity.vo;

import lombok.Data;

/**
 * @author kuro
 * @version V1.0
 * @date 2020-04-27 2:51 PM
 **/
@Data
public class RecoverServiceReqVO {
    private String appName;
    private String ip;
    private String port;
}
