package com.xkzhangsan.thread.pool.monitor.rejected;

import com.xkzhangsan.thread.pool.monitor.GlobalMonitor;
import com.xkzhangsan.thread.pool.monitor.ThreadPoolMonitor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 提交任务的线程会自己运行这个任务，同时打印告警日志 <br>
 *
 * @author xkzhangsan
 */
public class CallerRunsPolicyAlarm extends ThreadPoolExecutor.CallerRunsPolicy {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        ThreadPoolMonitor threadPoolMonitor = (ThreadPoolMonitor) e;
        if (threadPoolMonitor.isRejectedAlarm()) {
            long nowTimestamp = System.currentTimeMillis();
            if (threadPoolMonitor.getRejectedAlarmTimestamp() == 0) {
                System.out.println("CallerRunsPolicy warning poolName:" + threadPoolMonitor.getPoolName());
                threadPoolMonitor.setRejectedAlarmTimestamp(nowTimestamp);
            } else if (nowTimestamp - threadPoolMonitor.getRejectedAlarmTimestamp() > GlobalMonitor.ALARM_PERIOD) {
                threadPoolMonitor.setRejectedAlarmTimestamp(nowTimestamp);
                System.out.println("CallerRunsPolicy warning poolName:" + threadPoolMonitor.getPoolName());
            }
        } else {
            System.out.println("CallerRunsPolicy warning poolName:" + threadPoolMonitor.getPoolName());
        }
        super.rejectedExecution(r, e);
    }
}
