package com.xkzhangsan.thread.pool.monitor;

import com.xkzhangsan.thread.pool.monitor.constant.MonitorLevelEnum;

import java.util.List;
import java.util.concurrent.*;

/**
 * 线程池监控器 <br>
 * 支持监控基本情况和任务执行情况<br>
 *
 * @author xkzhangsan
 */
public class ThreadPoolMonitor extends ThreadPoolExecutor {

    private String poolName;

    private MonitorLevelEnum monitorLevel = MonitorLevelEnum.NONE;

    private ConcurrentHashMap<String, Long> taskStartTimeMap;

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
        if (isTaskMonitor()) {
            //TODO 增加线程名称
            taskStartTimeMap.put(String.valueOf(r.hashCode()), System.currentTimeMillis());
        }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        if (isTaskMonitor()) {
            Long startTime = taskStartTimeMap.remove(String.valueOf(r.hashCode()));
            if (startTime == null) {
                return;
            }
            System.out.println("poolName:" + poolName + " task:" + r.hashCode() + " cost:" + (System.currentTimeMillis() - startTime));
        }
    }

    @Override
    public void shutdown() {
        super.shutdown();
        //停止时，已经提交未完成的任务仍在运行，不能清理taskStartTimeMap缓存
    }

    @Override
    public List<Runnable> shutdownNow() {
        List<Runnable> runnableList = super.shutdownNow();
        //停止时，终止所有任务，需要清理taskStartTimeMap缓存，否则会导致内存泄漏
        taskStartTimeMap = null;
        return runnableList;
    }

    private void init(String poolName, MonitorLevelEnum monitorLevel) {
        this.poolName = poolName;
        this.monitorLevel = monitorLevel;
        this.taskStartTimeMap = new ConcurrentHashMap<>();
    }

    private boolean isTaskMonitor() {
        return monitorLevel == MonitorLevelEnum.TASK || monitorLevel == MonitorLevelEnum.POOL_TASK
                || monitorLevel == MonitorLevelEnum.SUGGESTION;
    }

}
