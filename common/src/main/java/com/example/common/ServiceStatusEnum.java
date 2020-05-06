package com.example.common;

public enum ServiceStatusEnum {
    /**
     * 已删除
     */
    IN_SERVICE(1, "可用"),
    /**
     * 未删除
     */
    OUT_OF_SERVICE(0, "不可用"),
    ;

    private int status;

    private String desc;

    ServiceStatusEnum(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

    public static ServiceStatusEnum valueOf(int status) {
        ServiceStatusEnum[] values = ServiceStatusEnum.values();
        for (ServiceStatusEnum statusEnum : values) {
            if (status == statusEnum.getStatus()) {
                return statusEnum;
            }
        }
        return null;
    }
}
