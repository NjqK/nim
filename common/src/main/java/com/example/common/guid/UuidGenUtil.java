package com.example.common.guid;

/**
 * 系统UUID生成器
 *
 * @author kuro
 * @version v1.0
 * @date 20-3-9 下午5:51
 **/
public class UuidGenUtil {

    private static SnowFlakeIdGenerator generator = new SnowFlakeIdGenerator(1, 1);

    /**
     * 获得一个uuid
     *
     * @return
     */
    public static long getUUID() {
        return generator.nextId();
    }


    /**
     * 获得指定数量的UUID
     *
     * @param number
     * @return
     */
    public static long[] getUUIDs(int number) {
        if (number < 1) {
            throw new RuntimeException("生成uuid的数量至少大于1.");
        }
        long[] ss = new long[number];
        for (int i = 0; i < number; i++) {
            ss[i] = getUUID();
        }
        return ss;
    }
}
