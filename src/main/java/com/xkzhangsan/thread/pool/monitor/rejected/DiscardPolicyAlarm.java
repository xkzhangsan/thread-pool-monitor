package com.xkzhangsan.thread.pool.monitor.rejected;

import com.xkzhangsan.thread.pool.monitor.ThreadPoolMonitor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 直接丢弃新来的任务，同时打印告警日志 <br>
 *
 * @author xkzhangsan
 */
public class DiscardPolicyAlarm extends ThreadPoolExecutor.DiscardPolicy {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        ThreadPoolMonitor threadPoolMonitor = (ThreadPoolMonitor) e;
        System.out.println("DiscardPolicy warning poolName:" + threadPoolMonitor.getPoolName());
        super.rejectedExecution(r, e);
    }
}
