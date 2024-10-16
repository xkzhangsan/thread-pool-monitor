package com.xkzhangsan.thread.pool.monitor;

import com.xkzhangsan.thread.pool.monitor.constant.MonitorLevelEnum;
import com.xkzhangsan.thread.pool.monitor.rejected.AbortPolicyAlarm;
import com.xkzhangsan.thread.pool.monitor.rejected.CallerRunsPolicyAlarm;
import com.xkzhangsan.thread.pool.monitor.rejected.DiscardOldestPolicyAlarm;
import com.xkzhangsan.thread.pool.monitor.rejected.DiscardPolicyAlarm;

import java.util.List;
import java.util.concurrent.*;

/**
 * 线程池监控器 <br>
 * 支持监控基本情况和任务执行情况<br>
 *
 * @author xkzhangsan
 */
public class ThreadPoolMonitor extends ThreadPoolExecutor {

    /**
     * 线程池名称
     */
    private String poolName;
    /**
     * 线程池监控级别，默认不监控
     */
    private MonitorLevelEnum monitorLevel = MonitorLevelEnum.NONE;
    /**
     * 任务开始时间map
     */
    private ConcurrentHashMap<String, Long> taskStartTimeMap;
    /**
     * 线程池线程数量百分比告警值，比如 0.95
     */
    private double poolSizePercentageAlarm;
    /**
     * 线程池线程数量告警时间戳
     */
    private long poolSizeAlarmTimestamp;
    /**
     * 线程池线程数量告警抑制开关
     */
    private boolean poolSizeAlarmRestrainFlag;
    /**
     * 线程池队列数量百分比告警值，比如 0.95
     */
    private double queueSizePercentageAlarm;
    /**
     * 线程池队列数量告警时间戳
     */
    private long queueSizeAlarmTimestamp;
    /**
     * 线程池队列数量告警抑制开关
     */
    private boolean queueSizeAlarmRestrainFlag;
    /**
     * 线程池任务耗时告警值，单位毫秒
     */
    private long taskCostAlarm;
    /**
     * 线程池任务耗时告警时间戳，多线程更新使用volatile
     */
    private volatile long taskCostAlarmTimestamp;
    /**
     * 线程池队列数量告警抑制开关
     */
    private boolean taskCostAlarmRestrainFlag;
    /**
     * 队列最大值
     */
    private int queueCapacity;
    /**
     * 是否支持任务拒绝告警
     */
    private boolean rejectedAlarm;
    /**
     * 任务拒绝告警时间戳，多线程更新使用volatile
     */
    private volatile long rejectedAlarmTimestamp;
    /**
     * 任务拒绝告警抑制开关
     */
    private boolean rejectedAlarmRestrainFlag;

    public ThreadPoolMonitor(int corePoolSize, int maximumPoolSize, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, 0, TimeUnit.MILLISECONDS, workQueue);
    }

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
    public ThreadPoolMonitor(int corePoolSize, int maximumPoolSize, BlockingQueue<Runnable> workQueue, String poolName, MonitorLevelEnum monitorLevel) {
        super(corePoolSize, maximumPoolSize, 0, TimeUnit.MILLISECONDS, workQueue);
        init(poolName, monitorLevel);
    }

    public ThreadPoolMonitor(int corePoolSize, int maximumPoolSize, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler, String poolName, MonitorLevelEnum monitorLevel) {
        super(corePoolSize, maximumPoolSize, 0, TimeUnit.MILLISECONDS, workQueue, handler);
        init(poolName, monitorLevel);
    }

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
            long cost = System.currentTimeMillis() - startTime;
            System.out.println("poolName:" + poolName + " task:" + r.hashCode() + " cost:" + cost);
            if (taskCostAlarm > 0 && cost > taskCostAlarm) {
                if (isTaskCostAlarmRestrainFlag()) {
                    long nowTimestamp = System.currentTimeMillis();
                    if (taskCostAlarmTimestamp == 0) {
                        System.out.println("===== taskCost warning poolName:" + poolName + " task:" + r.hashCode() + " taskCostAlarm:" + taskCostAlarm + " cost:" + cost);
                        setTaskCostAlarmTimestamp(nowTimestamp);
                    } else if (nowTimestamp - taskCostAlarmTimestamp > GlobalMonitor.ALARM_PERIOD) {
                        setTaskCostAlarmTimestamp(nowTimestamp);
                        System.out.println("===== taskCost warning poolName:" + poolName + " task:" + r.hashCode() + " taskCostAlarm:" + taskCostAlarm + " cost:" + cost);
                    }
                } else {
                    System.out.println("===== taskCost warning poolName:" + poolName + " task:" + r.hashCode() + " taskCostAlarm:" + taskCostAlarm + " cost:" + cost);
                }
            }
        }
    }

    @Override
    public void shutdown() {
        super.shutdown();
        end(false);
    }

    @Override
    public List<Runnable> shutdownNow() {
        List<Runnable> runnableList = super.shutdownNow();
        end(true);
        return runnableList;
    }


    public ThreadPoolMonitor poolSizePercentageAlarm(double pooSizePercentageAlarm) {
        this.poolSizePercentageAlarm = pooSizePercentageAlarm;
        return this;
    }

    public long getPoolSizeAlarmTimestamp() {
        return poolSizeAlarmTimestamp;
    }

    public void setPoolSizeAlarmTimestamp(long poolSizeAlarmTimestamp) {
        this.poolSizeAlarmTimestamp = poolSizeAlarmTimestamp;
    }

    public boolean isPoolSizeAlarmRestrainFlag() {
        return poolSizeAlarmRestrainFlag;
    }

    public ThreadPoolMonitor poolSizeAlarmRestrainFlag(boolean poolSizeAlarmRestrainFlag) {
        this.poolSizeAlarmRestrainFlag = poolSizeAlarmRestrainFlag;
        return this;
    }

    public ThreadPoolMonitor queueSizePercentageAlarm(double queueSizePercentageAlarm) {
        this.queueSizePercentageAlarm = queueSizePercentageAlarm;
        return this;
    }

    public long getQueueSizeAlarmTimestamp() {
        return queueSizeAlarmTimestamp;
    }

    public void setQueueSizeAlarmTimestamp(long queueSizeAlarmTimestamp) {
        this.queueSizeAlarmTimestamp = queueSizeAlarmTimestamp;
    }

    public boolean isQueueSizeAlarmRestrainFlag() {
        return queueSizeAlarmRestrainFlag;
    }

    public ThreadPoolMonitor queueSizeAlarmRestrainFlag(boolean queueSizeAlarmRestrainFlag) {
        this.queueSizeAlarmRestrainFlag = queueSizeAlarmRestrainFlag;
        return this;
    }

    public long getTaskCostAlarmTimestamp() {
        return taskCostAlarmTimestamp;
    }

    public void setTaskCostAlarmTimestamp(long taskCostAlarmTimestamp) {
        this.taskCostAlarmTimestamp = taskCostAlarmTimestamp;
    }

    public boolean isTaskCostAlarmRestrainFlag() {
        return taskCostAlarmRestrainFlag;
    }

    public ThreadPoolMonitor taskCostAlarmRestrainFlag(boolean taskCostAlarmRestrainFlag) {
        this.taskCostAlarmRestrainFlag = taskCostAlarmRestrainFlag;
        return this;
    }

    public ThreadPoolMonitor taskCostAlarm(long taskCostAlarm) {
        this.taskCostAlarm = taskCostAlarm;
        return this;
    }

    public long getRejectedAlarmTimestamp() {
        return rejectedAlarmTimestamp;
    }

    public void setRejectedAlarmTimestamp(long rejectedAlarmTimestamp) {
        this.rejectedAlarmTimestamp = rejectedAlarmTimestamp;
    }

    public boolean isRejectedAlarmRestrainFlag() {
        return rejectedAlarmRestrainFlag;
    }

    public ThreadPoolMonitor rejectedAlarmRestrainFlag(boolean rejectedAlarmRestrainFlag) {
        this.rejectedAlarmRestrainFlag = rejectedAlarmRestrainFlag;
        return this;
    }

    public ThreadPoolMonitor rejectedAlarm(boolean rejectedAlarm) {
        this.rejectedAlarm = rejectedAlarm;
        if (rejectedAlarm) {
            RejectedExecutionHandler rejectedExecutionHandler = super.getRejectedExecutionHandler();
            if (rejectedExecutionHandler.getClass().equals(AbortPolicy.class)) {
                super.setRejectedExecutionHandler(new AbortPolicyAlarm());
            } else if (rejectedExecutionHandler.getClass().equals(DiscardOldestPolicy.class)) {
                super.setRejectedExecutionHandler(new DiscardOldestPolicyAlarm());
            } else if (rejectedExecutionHandler.getClass().equals(DiscardPolicy.class)) {
                super.setRejectedExecutionHandler(new DiscardPolicyAlarm());
            } else if (rejectedExecutionHandler.getClass().equals(CallerRunsPolicy.class)) {
                super.setRejectedExecutionHandler(new CallerRunsPolicyAlarm());
            }
        }
        return this;
    }

    public boolean isRejectedAlarm() {
        return rejectedAlarm;
    }

    public String getPoolName() {
        return poolName;
    }

    public MonitorLevelEnum getMonitorLevel() {
        return monitorLevel;
    }

    public double getPoolSizePercentageAlarm() {
        return poolSizePercentageAlarm;
    }

    public double getQueueSizePercentageAlarm() {
        return queueSizePercentageAlarm;
    }

    public long getTaskCostAlarm() {
        return taskCostAlarm;
    }

    private void init(String poolName, MonitorLevelEnum monitorLevel) {
        this.poolName = poolName;
        this.monitorLevel = monitorLevel;
        this.taskStartTimeMap = new ConcurrentHashMap<>();
        if (isPoolMonitor()) {
            GlobalMonitor.getInstance().put(poolName, this);
        }
        this.queueCapacity = super.getQueue().remainingCapacity();
    }

    public int getQueueCapacity() {
        return this.queueCapacity;
    }

    public String getStatus() {
        if (super.isTerminated()) {
            return "Terminated";
        } else if (super.isShutdown()) {
            return "Shutting down";
        } else {
            return "Running";
        }
    }

    private boolean isTaskMonitor() {
        return monitorLevel == MonitorLevelEnum.TASK || monitorLevel == MonitorLevelEnum.POOL_TASK;
    }

    private boolean isPoolMonitor() {
        return monitorLevel == MonitorLevelEnum.POOL || monitorLevel == MonitorLevelEnum.POOL_TASK;
    }

    private void end(boolean isNow) {
        if (isNow) {
            //停止时，需要清理taskStartTimeMap缓存，否则会导致内存泄漏
            taskStartTimeMap = null;
        }
        //停止时，需要清理threadPoolMonitorMap缓存，否则会导致内存泄漏
        if (isPoolMonitor()) {
            GlobalMonitor.getInstance().remove(poolName);
        }
    }

}
