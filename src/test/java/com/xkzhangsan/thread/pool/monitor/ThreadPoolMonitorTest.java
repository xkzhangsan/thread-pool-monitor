package com.xkzhangsan.thread.pool.monitor;

import com.xkzhangsan.thread.pool.monitor.constant.MonitorLevelEnum;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ThreadPoolMonitorTest {

    public static void main(String[] args) throws InterruptedException {
        poolMonitor();
    }

    public static void poolMonitor() throws InterruptedException {
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
        //因为调用shutdown方法会将threadPoolMonitor从监控缓存中删除，这里sleep 100s
        TimeUnit.SECONDS.sleep(100);
        //线程池必须手动关闭，否则一直运行
        threadPoolMonitor.shutdown();
    }

    public static void taskMonitor() throws InterruptedException {
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
        //因为调用shutdown方法会将threadPoolMonitor从监控缓存中删除，这里sleep 100s
        TimeUnit.SECONDS.sleep(100);
        //线程池必须手动关闭，否则一直运行
        threadPoolMonitor.shutdown();
    }

    public static void poolTaskMonitor() throws InterruptedException {
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
        //因为调用shutdown方法会将threadPoolMonitor从监控缓存中删除，这里sleep 100s
        TimeUnit.SECONDS.sleep(100);
        //线程池必须手动关闭，否则一直运行
        threadPoolMonitor.shutdown();
    }

}
