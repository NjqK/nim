package com.example.common;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-9 下午3:57
 **/
public enum IsDeleteEnum {

    /**
     * 已删除
     */
    YES(true),
    /**
     * 未删除
     */
    NO(false),
    ;

    private boolean value;

    IsDeleteEnum(boolean bool) {
        value = bool;
    }

    public boolean getValue() {
        return value;
    }
}
