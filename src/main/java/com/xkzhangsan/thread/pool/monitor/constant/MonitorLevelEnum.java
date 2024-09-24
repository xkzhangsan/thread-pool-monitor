package com.xkzhangsan.thread.pool.monitor.constant;

public enum MonitorLevelEnum {
    /**
     * 不监控，默认值
     */
    NONE,
    /**
     * 监控基本信息
     */
    POOL,
    /**
     * 监控任务信息
     */
    TASK,
    /**
     * 同时监控基本信息和任务信息
     */
    POOL_TASK,
    /**
     * 同时监控基本信息和任务信息，并给出优化建议
     */
    SUGGESTION;
}
