package com.xcdh.target.lock.normal.utilDemoA;

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

    public static final long TIMEOUT_MILLIS = 30000;

    public static final int RETRY_TIMES = Integer.MAX_VALUE;

    public static final long SLEEP_MILLIS = 500;

    public boolean lock(String key, long expire, int retryTimes, long sleepMillis);

    public boolean releaseLock(String key);
}