package com.xkzhangsan.thread.pool.monitor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 全局监控 <br>
 * 1.定期记录线程池基本信息，并告警 <br>
 * 2.记录告警记录，支持配置抑制时间，防止频繁告警 TODO <br>
 * 3.自定义日志级别，默认情况：全部输出debug，告警输出warn，支持配置包括高级阈值和抑制时间。 TODO
 *
 * @author xkzhangsan
 */
public class GlobalMonitor {
    private static volatile GlobalMonitor instance;
    private static final ConcurrentHashMap<String, ThreadPoolMonitor> threadPoolMonitorMap = new ConcurrentHashMap<>();
    private static final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);

    private GlobalMonitor() {
        scheduledThreadPoolExecutor.scheduleAtFixedRate(new PoolInfoRunnable(), 10, 10, TimeUnit.SECONDS);
    }

    public static GlobalMonitor getInstance() {
        if (instance == null) {
            synchronized (GlobalMonitor.class) {
                if (instance == null) {
                    instance = new GlobalMonitor();
                }
            }
        }
        return instance;
    }

    public void print() {
        System.out.println("instance start");
    }

    public void add(String poolName, ThreadPoolMonitor threadPoolMonitor) {
        threadPoolMonitorMap.put(poolName, threadPoolMonitor);
    }

    static class PoolInfoRunnable implements Runnable {

        @Override
        public void run() {
            threadPoolMonitorMap.forEach((k, v) -> {
                int currentPoolSize = v.getPoolSize();
                int queueSize = v.getQueue().size();
                System.out.println("poolName:" + k + " status:" + v.getStatus() + " corePoolSize:" + v.getCorePoolSize() + " maximumPoolSize:"
                        + v.getMaximumPoolSize() + " currentPoolSize:" + currentPoolSize + " queueCapacity:" + v.getQueueCapacity()
                        + " queueSize:" + queueSize);
                if (v.getPoolSizePercentageAlarm() > 0) {
                    double percent = (double) currentPoolSize / v.getMaximumPoolSize();
                    if (percent > v.getPoolSizePercentageAlarm()) {
                        System.out.println("===== poolSize warning poolName:" + k + " poolSizePercentageAlarm:" + v.getPoolSizePercentageAlarm() + " percent:" + percent);
                    }
                }
                if (v.getQueueSizePercentageAlarm() > 0) {
                    double percent = (double) queueSize / v.getQueueCapacity();
                    if (percent > v.getQueueSizePercentageAlarm()) {
                        System.out.println("===== queueSize warning poolName:" + k + " queueSizePercentageAlarm:" + v.getQueueSizePercentageAlarm() + " percent:" + percent);
                    }
                }
            });
        }
    }

    public static void main(String[] args) {
        GlobalMonitor.getInstance().print();
    }
}
