package com.example.connector.common;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author kuro
 * @version V1.0
 * @date 2020-04-27 1:19 PM
 **/
public class ConnectorThreadFactory {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(5);

    public static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newScheduledThreadPool(1);

    public static void addJob(Runnable job) {
        EXECUTOR_SERVICE.submit(job);
    }

    public static void  addScheduledJob(Runnable job, long scheduleTime, TimeUnit timeUnit) {
        SCHEDULED_EXECUTOR_SERVICE.scheduleAtFixedRate(job, 0L, scheduleTime, timeUnit);
    }
}
