package com.xkzhangsan.thread.pool.monitor;

import com.xkzhangsan.thread.pool.monitor.constant.MonitorLevelEnum;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolAlarmTest {

    public static void main(String[] args) throws InterruptedException {
        rejectedAlarmCallerRunsPolicyRestrain();
    }

    public static void poolSizeAlarm() throws InterruptedException {
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
        //因为调用shutdown方法会将threadPoolMonitor从监控缓存中删除，这里sleep 100s
        TimeUnit.SECONDS.sleep(100);
        //线程池必须手动关闭，否则一直运行
        threadPoolMonitor.shutdown();
    }

    public static void taskAlarm() throws InterruptedException {
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
        //因为调用shutdown方法会将threadPoolMonitor从监控缓存中删除，这里sleep 100s
        TimeUnit.SECONDS.sleep(100);
        //线程池必须手动关闭，否则一直运行
        threadPoolMonitor.shutdown();
    }

    public static void poolQueueAlarm() throws InterruptedException {
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
        //因为调用shutdown方法会将threadPoolMonitor从监控缓存中删除，这里sleep 100s
        TimeUnit.SECONDS.sleep(100);
        //线程池必须手动关闭，否则一直运行
        threadPoolMonitor.shutdown();
    }

    public static void rejectedAlarmAbortPolicy() throws InterruptedException {
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
        //因为调用shutdown方法会将threadPoolMonitor从监控缓存中删除，这里sleep 100s
        TimeUnit.SECONDS.sleep(100);
        //线程池必须手动关闭，否则一直运行
        threadPoolMonitor.shutdown();
    }

    public static void rejectedAlarmDiscardPolicy() throws InterruptedException {
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
        //因为调用shutdown方法会将threadPoolMonitor从监控缓存中删除，这里sleep 100s
        TimeUnit.SECONDS.sleep(100);
        //线程池必须手动关闭，否则一直运行
        threadPoolMonitor.shutdown();
    }

    public static void rejectedAlarmDiscardOldestPolicy() throws InterruptedException {
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
        //因为调用shutdown方法会将threadPoolMonitor从监控缓存中删除，这里sleep 100s
        TimeUnit.SECONDS.sleep(100);
        //线程池必须手动关闭，否则一直运行
        threadPoolMonitor.shutdown();
    }

    public static void rejectedAlarmCallerRunsPolicy() throws InterruptedException {
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
        //因为调用shutdown方法会将threadPoolMonitor从监控缓存中删除，这里sleep 100s
        TimeUnit.SECONDS.sleep(100);
        //线程池必须手动关闭，否则一直运行
        threadPoolMonitor.shutdown();
    }

    public static void poolSizeAlarmRestrain() throws InterruptedException {
        ThreadPoolMonitor threadPoolMonitor = new ThreadPoolMonitor(1, 3,
                new ArrayBlockingQueue<>(100), "test", MonitorLevelEnum.POOL)
                .poolSizePercentageAlarm(0.2)
                .poolSizeAlarmRestrainFlag(true);
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
        //因为调用shutdown方法会将threadPoolMonitor从监控缓存中删除，这里sleep 100s
        TimeUnit.SECONDS.sleep(100);
        //线程池必须手动关闭，否则一直运行
        threadPoolMonitor.shutdown();
    }

    public static void queueSizeAlarmRestrain() throws InterruptedException {
        ThreadPoolMonitor threadPoolMonitor = new ThreadPoolMonitor(1, 3,
                new ArrayBlockingQueue<>(100), "test", MonitorLevelEnum.POOL)
                .queueSizePercentageAlarm(0.8)
                .queueSizeAlarmRestrainFlag(true);
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
        //因为调用shutdown方法会将threadPoolMonitor从监控缓存中删除，这里sleep 100s
        TimeUnit.SECONDS.sleep(100);
        //线程池必须手动关闭，否则一直运行
        threadPoolMonitor.shutdown();
    }

    public static void taskAlarmRestrain() throws InterruptedException {
        ThreadPoolMonitor threadPoolMonitor = new ThreadPoolMonitor(1, 3,
                new ArrayBlockingQueue<>(100), "test", MonitorLevelEnum.TASK)
                .taskCostAlarm(2000)
                .taskCostAlarmRestrainFlag(true);
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
        //因为调用shutdown方法会将threadPoolMonitor从监控缓存中删除，这里sleep 100s
        TimeUnit.SECONDS.sleep(100);
        //线程池必须手动关闭，否则一直运行
        threadPoolMonitor.shutdown();
    }

    public static void rejectedAlarmCallerRunsPolicyRestrain() throws InterruptedException {
        ThreadPoolMonitor threadPoolMonitor = new ThreadPoolMonitor(1, 3,
                new ArrayBlockingQueue<>(1), new ThreadPoolExecutor.CallerRunsPolicy(), "test", MonitorLevelEnum.POOL)
                .rejectedAlarm(true)
                .rejectedAlarmRestrainFlag(true);
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
        //因为调用shutdown方法会将threadPoolMonitor从监控缓存中删除，这里sleep 100s
        TimeUnit.SECONDS.sleep(100);
        //线程池必须手动关闭，否则一直运行
        threadPoolMonitor.shutdown();
    }
}
