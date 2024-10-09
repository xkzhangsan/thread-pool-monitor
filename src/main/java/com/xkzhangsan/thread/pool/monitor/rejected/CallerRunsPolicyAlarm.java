package com.xkzhangsan.thread.pool.monitor.rejected;

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
        System.out.println("CallerRunsPolicy warning poolName:" + threadPoolMonitor.getPoolName());
        super.rejectedExecution(r, e);
    }
}
