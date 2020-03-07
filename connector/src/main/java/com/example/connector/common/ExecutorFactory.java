package com.example.connector.common;

import java.util.concurrent.*;

/**
 * @author kuro
 * @version v1.0
 * @date 20-3-7 下午5:18
 **/
public class ExecutorFactory {

    public static ExecutorService singleExecutor(String threadName) {
        return new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, threadName);
            }
        });
    }
}
