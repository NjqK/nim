package com.example.connector.common;

import java.util.concurrent.*;

/**
 * @author kuro
 * @version V1.0
 * @date 2020-04-27 1:19 PM
 **/
public class ConnectorThreadFactory {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(5);

    public static void addJob(Runnable job) {
        EXECUTOR_SERVICE.submit(job);
    }
}
