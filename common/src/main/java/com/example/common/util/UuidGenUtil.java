package com.example.common.util;

import com.example.common.SnowFlakeIdGenerator;

import java.util.HashMap;
import java.util.UUID;

/**
 * TODO 用数字的
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

    public static void main(String[] args) {
        SnowFlakeIdGenerator generator = new SnowFlakeIdGenerator(1, 1);
        long start = System.currentTimeMillis();
        int initialCapacity = 1000000;
        int i = 0;
        HashMap<Long, Long> hashMap = new HashMap<>(initialCapacity);
        while (initialCapacity > 0) {
            ++i;
            --initialCapacity;
            Long uuid = generator.nextId();
            if (hashMap.get(uuid) != null) {
                System.out.println("FUCK " + i);
                break;
            }
            hashMap.put(uuid, uuid);
        }
        System.out.println("cost:" + (System.currentTimeMillis() - start) + "ms ");
    }
}
