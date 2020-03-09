package com.example.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * @author kuro
 * @version V1.0
 * @date 3/5/20 8:19 PM
 **/
@Slf4j
public class ZkClientUtil {

    private static ZooKeeper zooKeeper;

    public static boolean start(String zkUrl) {
        try {
            zooKeeper = new ZooKeeper(zkUrl, 20000, null);
        } catch (IOException e) {
            log.error("创建ZkClient失败！");
            return false;
        }
        return true;
    }

    public static boolean create(String path) {
        try {
            Stat exists = zooKeeper.exists(path, true);
            if (exists == null) {
                // 不存在
            } else {

            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}