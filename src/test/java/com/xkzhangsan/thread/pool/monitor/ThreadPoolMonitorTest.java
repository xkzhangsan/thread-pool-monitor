package com.xkzhangsan.thread.pool.monitor;

import com.xkzhangsan.thread.pool.monitor.constant.MonitorLevelEnum;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ThreadPoolMonitorTest {

    public static void main(String[] args) {
        taskMonitor();
    }

    public static void taskMonitor() {
        ThreadPoolMonitor threadPoolMonitor = new ThreadPoolMonitor(1, 3, 0, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(5), "test", MonitorLevelEnum.TASK);
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            threadPoolMonitor.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(finalI);
            });
        }
        threadPoolMonitor.shutdown();
    }
}
