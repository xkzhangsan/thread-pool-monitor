package com.xkzhangsan.thread.pool.monitor;

import com.xkzhangsan.thread.pool.monitor.constant.MonitorLevelEnum;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolAlarmTest {

    public static void main(String[] args) {
        rejectedAlarmCallerRunsPolicy();
    }

    public static void poolSizeAlarm() {
        ThreadPoolMonitor threadPoolMonitor = new ThreadPoolMonitor(1, 3,
                new ArrayBlockingQueue<>(100), "test", MonitorLevelEnum.POOL)
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
                new ArrayBlockingQueue<>(100), "test", MonitorLevelEnum.TASK)
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
                new ArrayBlockingQueue<>(100), "test", MonitorLevelEnum.POOL)
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

    public static void rejectedAlarmAbortPolicy() {
        ThreadPoolMonitor threadPoolMonitor = new ThreadPoolMonitor(1, 3,
                new ArrayBlockingQueue<>(1), "test", MonitorLevelEnum.POOL)
                .rejectedAlarm(true);
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

    public static void rejectedAlarmDiscardPolicy() {
        ThreadPoolMonitor threadPoolMonitor = new ThreadPoolMonitor(1, 3,
                new ArrayBlockingQueue<>(1), new ThreadPoolExecutor.DiscardPolicy(), "test", MonitorLevelEnum.POOL)
                .rejectedAlarm(true);
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

    public static void rejectedAlarmDiscardOldestPolicy() {
        ThreadPoolMonitor threadPoolMonitor = new ThreadPoolMonitor(1, 3,
                new ArrayBlockingQueue<>(1), new ThreadPoolExecutor.DiscardOldestPolicy(), "test", MonitorLevelEnum.POOL)
                .rejectedAlarm(true);
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

    public static void rejectedAlarmCallerRunsPolicy() {
        ThreadPoolMonitor threadPoolMonitor = new ThreadPoolMonitor(1, 3,
                new ArrayBlockingQueue<>(1), new ThreadPoolExecutor.CallerRunsPolicy(), "test", MonitorLevelEnum.POOL)
                .rejectedAlarm(true);
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
