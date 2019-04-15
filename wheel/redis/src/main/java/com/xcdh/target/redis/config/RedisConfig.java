package com.xcdh.target.redis.config;

/**
 * RedisConfig
 *
 * @author wangbo
 * @version 1.0 2017/6/24
 */
public class RedisConfig {

    private int maxWaitMillis;
    private int maxTotal;
    private int minIdle;
    private int maxIdle;
    private int timeOut;

    public RedisConfig(int maxWaitMillis, int maxTotal, int minIdle, int maxIdle, int timeOut) {
        this.maxWaitMillis = maxWaitMillis;
        this.maxTotal = maxTotal;
        this.minIdle = minIdle;
        this.maxIdle = maxIdle;
        this.timeOut = timeOut;
    }

    public int getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(int maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }
}
