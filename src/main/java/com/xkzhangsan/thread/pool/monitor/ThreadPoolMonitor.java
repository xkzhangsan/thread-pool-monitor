package com.xkzhangsan.thread.pool.monitor;

import com.xkzhangsan.thread.pool.monitor.constant.MonitorLevelEnum;

import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * 线程池监控器 <br>
 * 支持监控基本情况和任务执行情况<br>
 * @author xkzhangsan
 */
public class ThreadPoolMonitor extends ThreadPoolExecutor {

    private String poolName;

    private MonitorLevelEnum monitorLevel;

    private ConcurrentHashMap<String, Date> taskStartTimeMap;

    public ThreadPoolMonitor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public ThreadPoolMonitor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public ThreadPoolMonitor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public ThreadPoolMonitor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    //overload
    public ThreadPoolMonitor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, String poolName, MonitorLevelEnum monitorLevel) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        init(poolName, monitorLevel);
    }

    public ThreadPoolMonitor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, String poolName, MonitorLevelEnum monitorLevel) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        init(poolName, monitorLevel);
    }

    public ThreadPoolMonitor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler, String poolName, MonitorLevelEnum monitorLevel) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        init(poolName, monitorLevel);
    }

    public ThreadPoolMonitor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler, String poolName, MonitorLevelEnum monitorLevel) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        init(poolName, monitorLevel);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        if (monitorLevel == MonitorLevelEnum.TASK || monitorLevel == MonitorLevelEnum.POOL_TASK
                || monitorLevel == MonitorLevelEnum.SUGGESTION) {
            //TODO 增加线程名称
            taskStartTimeMap.put(String.valueOf(r.hashCode()), new Date());
        }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        if (monitorLevel == MonitorLevelEnum.TASK || monitorLevel == MonitorLevelEnum.POOL_TASK
                || monitorLevel == MonitorLevelEnum.SUGGESTION) {
            Date date = taskStartTimeMap.remove(String.valueOf(r.hashCode()));
            if (date == null) {
                return;
            }
            System.out.println("poolName:" + poolName + " task:" + r.hashCode() + " cost:" + ((new Date()).getTime() - date.getTime()));
        }
    }

    @Override
    public void shutdown() {
        super.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return super.shutdownNow();
    }

    private void init(String poolName, MonitorLevelEnum monitorLevel) {
        this.poolName = poolName;
        this.monitorLevel = monitorLevel;
        this.taskStartTimeMap = new ConcurrentHashMap<>();
    }

}
