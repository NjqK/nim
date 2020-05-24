package com.example.chat.common;

/**
 * @author kuro
 * @version V1.0
 * @date 3/6/20 1:07 PM
 **/
public enum ServerTypeEnum {

    /**
     * chat server
     */
    CHAT("Chat", 0),
    /**
     * push server
     */
    PUSH("Push", 2),
    /**
     * connector server
     */
    CONNECTOR("Connector", 1)
    ;

    private String serverType;
    private Integer code;

    ServerTypeEnum(String serverType, Integer code) {
        this.serverType = serverType;
        this.code = code;
    }

    public String getServerType() {
        return serverType;
    }

    public Integer getCode() {
        return code;
    }

    public static ServerTypeEnum parse(String type) {
        for (ServerTypeEnum serverTypeEnum : ServerTypeEnum.values()) {
            if (type.equalsIgnoreCase(serverTypeEnum.serverType)) {
                return serverTypeEnum;
            }
        }
        return null;
    }

    public static ServerTypeEnum parse(Integer code) {
        for (ServerTypeEnum serverTypeEnum : ServerTypeEnum.values()) {
            if (code.equals(serverTypeEnum.code)) {
                return serverTypeEnum;
            }
        }
        return null;
    }
}
