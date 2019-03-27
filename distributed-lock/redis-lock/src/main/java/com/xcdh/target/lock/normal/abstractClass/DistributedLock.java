package com.xcdh.target.lock.normal.abstractClass;

/**
 * Project: Wheel
 *
 * Description:
 *
 * @author: MikeLC
 *
 * @date: 2019/3/26 19:14
 **/
public interface  DistributedLock {
    //获取分布式锁超时时间
    public static final long TIMEOUT_MILLIS = 30000;
    //获取锁重试间隔等待时间
    public static final long SLEEP_MILLIS = 500;
    //获取锁重试次数
    public static final int RETRY_TIMES = Integer.MAX_VALUE;
    //硬性锁重试次数
    public static final int HARD_RETRY_TIMES = 0;
    //加锁成功返回值
    public static final String LOCK_SUCCESS = "OK";
    //解锁成功返回值
    public static final String RELEASE_SUCCESS = "OK";
    //
    public static final String SET_IF_NOT_EXIST = "NX";
    //
    public static final String SET_WITH_EXPIRE_TIME = "PX";

    /**
     * 默认加锁方法：其它参数按照默认值；
     * @param key
     * @return
     */
    public boolean lock(String key);

    /**
     * 获取硬性锁，无重试机制；
     * @param key
     * @return
     */
    public boolean hardLock(String key);

    /**
     * 加锁：指定重试次数
     * @param key
     * @param retryTimes
     * @return
     */
    public boolean lock(String key, int retryTimes);

    /**
     * 加锁：指定重试次数&重试间隔时间
     * @param key
     * @param retryTimes
     * @param sleepMillis
     * @return
     */
    public boolean lock(String key, int retryTimes, long sleepMillis);

    /**
     * 加锁：指定锁持有时间
     * @param key
     * @param expire
     * @return
     */
    public boolean lock(String key, long expire);

    /**
     * 加锁：指定锁持有时间和重试次数
     * @param key
     * @param expire
     * @param retryTimes
     * @return
     */
    public boolean lock(String key, long expire, int retryTimes);

    /**
     * 加锁：指定锁持有时间、重试次数、重试间隔时间
     * @param key
     * @param expire
     * @param retryTimes
     * @param sleepMillis
     * @return
     */
    public boolean lock(String key, long expire, int retryTimes, long sleepMillis);

    /**
     * 解锁
     * @param key
     * @return
     */
    public boolean releaseLock(String key);

}