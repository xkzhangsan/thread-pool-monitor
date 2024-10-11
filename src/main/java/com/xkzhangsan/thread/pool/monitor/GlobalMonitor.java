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
    public static final long POOL_MONITOR_PERIOD = 10;
    public static final long ALARM_PERIOD = 300000;
    private static volatile GlobalMonitor instance;
    private static final ConcurrentHashMap<String, ThreadPoolMonitor> threadPoolMonitorMap = new ConcurrentHashMap<>();
    private static final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);

    private GlobalMonitor() {
        scheduledThreadPoolExecutor.scheduleAtFixedRate(new PoolInfoRunnable(), POOL_MONITOR_PERIOD, POOL_MONITOR_PERIOD, TimeUnit.SECONDS);
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

    public void put(String poolName, ThreadPoolMonitor threadPoolMonitor) {
        threadPoolMonitorMap.put(poolName, threadPoolMonitor);
    }

    public void remove(String poolName) {
        threadPoolMonitorMap.remove(poolName);
    }

    static class PoolInfoRunnable implements Runnable {

        @Override
        public void run() {
            threadPoolMonitorMap.forEach((poolName, threadPoolMonitor) -> {
                int currentPoolSize = threadPoolMonitor.getPoolSize();
                int queueSize = threadPoolMonitor.getQueue().size();
                System.out.println("poolName:" + poolName + " status:" + threadPoolMonitor.getStatus() + " corePoolSize:" + threadPoolMonitor.getCorePoolSize() + " maximumPoolSize:"
                        + threadPoolMonitor.getMaximumPoolSize() + " currentPoolSize:" + currentPoolSize + " queueCapacity:" + threadPoolMonitor.getQueueCapacity()
                        + " queueSize:" + queueSize);
                if (threadPoolMonitor.getPoolSizePercentageAlarm() > 0) {
                    double percent = (double) currentPoolSize / threadPoolMonitor.getMaximumPoolSize();
                    if (percent > threadPoolMonitor.getPoolSizePercentageAlarm()) {
                        if (threadPoolMonitor.isPoolSizeAlarmRestrainFlag()) {
                            if (threadPoolMonitor.getPoolSizeAlarmTimestamp() == 0) {
                                threadPoolMonitor.setPoolSizeAlarmTimestamp(System.currentTimeMillis());
                                System.out.println("===== poolSize warning poolName:" + poolName + " poolSizePercentageAlarm:" + threadPoolMonitor.getPoolSizePercentageAlarm() + " percent:" + percent);
                            } else if (System.currentTimeMillis() - threadPoolMonitor.getPoolSizeAlarmTimestamp() > ALARM_PERIOD) {
                                threadPoolMonitor.setPoolSizeAlarmTimestamp(0);
                                System.out.println("===== poolSize warning poolName:" + poolName + " poolSizePercentageAlarm:" + threadPoolMonitor.getPoolSizePercentageAlarm() + " percent:" + percent);
                            }
                        } else {
                            System.out.println("===== poolSize warning poolName:" + poolName + " poolSizePercentageAlarm:" + threadPoolMonitor.getPoolSizePercentageAlarm() + " percent:" + percent);
                        }
                    }
                }
                if (threadPoolMonitor.getQueueSizePercentageAlarm() > 0) {
                    double percent = (double) queueSize / threadPoolMonitor.getQueueCapacity();
                    if (percent > threadPoolMonitor.getQueueSizePercentageAlarm()) {
                        System.out.println("===== queueSize warning poolName:" + poolName + " queueSizePercentageAlarm:" + threadPoolMonitor.getQueueSizePercentageAlarm() + " percent:" + percent);
                    }
                }
            });
        }
    }

}
