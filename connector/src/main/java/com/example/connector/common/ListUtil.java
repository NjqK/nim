package com.example.connector.common;

import java.util.List;

public class ListUtil {

    /**
     * 判斷list是不是空的
     *
     * @param list
     * @return
     */
    public static boolean isEmpty(List list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }
}
