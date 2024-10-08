package com.xkzhangsan.thread.pool.monitor;

import com.xkzhangsan.thread.pool.monitor.constant.MonitorLevelEnum;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ThreadPoolMonitorTest {

    public static void main(String[] args) {
        poolQueueAlarm();
    }

    public static void taskMonitor() {
        ThreadPoolMonitor threadPoolMonitor = new ThreadPoolMonitor(1, 3, 0, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(1000), "test", MonitorLevelEnum.TASK);
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            threadPoolMonitor.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(finalI);
            });
        }
        threadPoolMonitor.shutdown();
    }

    public static void poolMonitor() {
        ThreadPoolMonitor threadPoolMonitor = new ThreadPoolMonitor(1, 3, 0, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(1000), "test", MonitorLevelEnum.POOL);
        for (int i = 0; i < 100; i++) {
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

    public static void poolTaskMonitor() {
        ThreadPoolMonitor threadPoolMonitor = new ThreadPoolMonitor(1, 3, 0, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(1000), "test", MonitorLevelEnum.POOL_TASK);
        for (int i = 0; i < 100; i++) {
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

    public static void poolSizeAlarm() {
        ThreadPoolMonitor threadPoolMonitor = new ThreadPoolMonitor(1, 3,
                new ArrayBlockingQueue<>(100), "test", MonitorLevelEnum.POOL_TASK)
                .poolSizePercentageAlarm(0.2);
        for (int i = 0; i < 100; i++) {
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

    public static void taskAlarm() {
        ThreadPoolMonitor threadPoolMonitor = new ThreadPoolMonitor(1, 3,
                new ArrayBlockingQueue<>(100), "test", MonitorLevelEnum.POOL_TASK)
                .taskCostAlarm(2000);
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            threadPoolMonitor.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(finalI);
            });
        }
        threadPoolMonitor.shutdown();
    }

    public static void poolQueueAlarm() {
        ThreadPoolMonitor threadPoolMonitor = new ThreadPoolMonitor(1, 3,
                new ArrayBlockingQueue<>(100), "test", MonitorLevelEnum.POOL_TASK)
                .queueSizePercentageAlarm(0.8);
        for (int i = 0; i < 100; i++) {
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
