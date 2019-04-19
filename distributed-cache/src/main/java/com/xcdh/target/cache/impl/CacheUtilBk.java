package com.xcdh.target.cache.impl;

import com.xcdh.target.cache.abstractClass.AbstractDistributedCache;
import com.xcdh.target.lock.normal.abstractClass.DistributedLock;
import com.xcdh.target.redis.RedisKey;
import com.xcdh.target.redis.RedisUtil;
import com.xcdh.target.util.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.function.Supplier;


/**
 * Project: ARK
 *
 * Description:
 *
 * @author: MikeLC
 *
 * @date: 2019/4/15 13:48
 **/
@Service("cacheUtilBk")
public class CacheUtilBk extends AbstractDistributedCache {

    private static Logger logger = LoggerFactory.getLogger(CacheUtilBk.class);

    @Resource
    protected RedisUtil redisUtil;
    @Autowired
    private DistributedLock distributedLock;

    /**
     * 获取string 缓存数据
     *
     * @param key
     * @return
     */
    @Override
    public String getStringFromCache(RedisKey key) {
        String result = null;
        try {
            result = this.redisUtil.get(key.getKey());
            if (StringUtils.isNotBlank(result) && isEmptyString(result)){
                logHitRefresh(key);
                return null;
            }
        } catch (Exception e) {
            logger.error("getStringFromCache, key:" + key.getKey(), e);
        }
        return result;
    }

    /**
     * 获取hashMap
     */
    @Override
    public Map<String, String> getHashFromCache(RedisKey key) {
        Map<String, String> result = null;
        try {
            result = this.redisUtil.hgetAll(key.getKey());
            if (!CollectionUtil.isEmpty(result) && isEmptyMap(result)){
                logHitRefresh(key);
                return null;
            }
        } catch (Exception e) {
            logger.error("getHashFromCache, key:" + key.getKey(), e);
        }
        return result;
    }

    /**
     * 刷新（删除）缓存
     * @param redisKey
     * @param supplier
     * @param <T>
     * @return
     */
    @Override
    public <T> T refreshLockLoad(RedisKey redisKey, Supplier<T> supplier) {
        try {
            int retryTime = 100;
            while (retryTime-- > 0){
                if (distributedLock.lock(redisKey.getLoadLockKey(), redisKey.getLoadLockExpireSeconds())) {
                    //
                    if (existsKey(redisKey)) {
                        this.delKey(redisKey);
                    }
                    return supplier.get();
                } else {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {}
                }
            }
        } catch (Exception e) {
            logger.error("redis异常", e);
            throw new RuntimeException("fail");
        } finally {
            distributedLock.releaseLock(redisKey.getLoadLockKey());
        }
        logger.error("获取数据库锁失败");
        throw new RuntimeException("fail");
    }

    /**
     * 缓存穿透：未启用refreshKey；
     *          外部调用方法需要在set缓存的时候如果业务结果为空则Set上述Empty方法结果值至主key中；
     *          并且查询时，由上述get相关方法进行过滤；
     * @param redisKey
     * @param supplier
     * @param timeOutMillSec
     * @param <T>
     * @return
     */
    @Override
    public <T> T cacheLockLoad(RedisKey redisKey, Supplier<T> supplier, long timeOutMillSec) {
        T result = null;
        long startTime = System.currentTimeMillis();
        try {
            int retryTime = 100;
            while (retryTime-- > 0){
                // doubleCheck之锁外判断-快速返回
                if (existsKey(redisKey)) {
                    return null;
                }
                if (distributedLock.lock(redisKey.getLoadLockKey(), redisKey.getLoadLockExpireSeconds())) {
                    // doubleCheck之锁外判断-快速返回
                    if (existsKey(redisKey)) {
                        return null;
                    }
                    // 执行自定义方法
                    T t = supplier.get();
                    logHitDataBase(redisKey);
                    return t;
                } else {
                    // 如果超时或者key中已有值，则快速返回；
                    if (existsKey(redisKey) || System.currentTimeMillis() - startTime >= timeOutMillSec) {
                        return null;
                    }
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {}
                }
            }
        } catch (Exception e) {
            logger.error("redis异常", e);
            throw new RuntimeException("fail");
        } finally {
            distributedLock.releaseLock(redisKey.getLoadLockKey());
        }
        logger.error("获取数据库锁失败");
        throw new RuntimeException("fail");
    }



}
