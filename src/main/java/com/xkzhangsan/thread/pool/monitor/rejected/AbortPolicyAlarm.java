package com.xkzhangsan.thread.pool.monitor.rejected;

import com.xkzhangsan.thread.pool.monitor.ThreadPoolMonitor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 拒绝任务并抛异常，同时打印告警日志 <br>
 *
 * @author xkzhangsan
 */
public class AbortPolicyAlarm extends ThreadPoolExecutor.AbortPolicy {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        ThreadPoolMonitor threadPoolMonitor = (ThreadPoolMonitor) e;
        System.out.println("AbortPolicy warning poolName:" + threadPoolMonitor.getPoolName());
        super.rejectedExecution(r, e);
    }
}
