package com.xcdh.target.cache.impl;

import com.xcdh.target.cache.abstractClass.AbstractDistributedCache;
import com.xcdh.target.cache.abstractClass.DistributedCache;
import com.xcdh.target.lock.normal.abstractClass.DistributedLock;
import com.xcdh.target.redis.RedisKey;
import com.xcdh.target.redis.RedisUtil;
import com.xcdh.target.util.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
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
@Service("cacheUtil")
public class CacheUtil extends AbstractDistributedCache {

    private static Logger logger = LoggerFactory.getLogger(CacheUtil.class);

    @Autowired
    protected RedisUtil redisUtil;
    @Autowired
    private DistributedLock distributedLock;

    @Override
    public String getStringFromCache(RedisKey key) {
        String result = null;
        try {
            result = this.redisUtil.get(key.getKey());
            if (StringUtils.isBlank(result) && existsRefreshKey(key)){
                logHitRefresh(key);
            }
        } catch (Exception e) {
            logger.error("getStringFromCache, key:" + key.getKey(), e);
        }
        return result;
    }

    @Override
    public Map<String, String> getHashFromCache(RedisKey key) {
        Map<String, String> result = null;
        try {
            result = this.redisUtil.hgetAll(key.getKey());
            if (CollectionUtil.isEmpty(result) && existsRefreshKey(key)){
                logHitRefresh(key);
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
                    if (existsKeyMulti(redisKey)) {
                        this.delKeyMulti(redisKey);
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
     * 缓存穿透：启用refreshKey来处理防穿透逻辑；
     *          外部调用在业务数据为空时不需要对主key有任何操作；
     *          防穿透逻辑在本方法作用域内即可完成；
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
                // doubleCheck之锁外判断-快速返回()
                /**
                 * 注：
                 * 1. 锁前的快速返回判断逻辑是Check key&refreshKey；
                 * 2. 但是外部调用前已经判断过key，此处仅判断refreshKey即可；
                 * 3. 结论：为了方便理解，此处做冗余判断，跟锁内判断保持一致；
                 */
                if (existsKeyMulti(redisKey)) {
                    return null;
                }
                if (distributedLock.lock(redisKey.getLoadLockKey(), redisKey.getLoadLockExpireSeconds())) {
                    // doubleCheck之锁外判断-快速返回
                    if (existsKeyMulti(redisKey)) {
                        return null;
                    }
                    // 执行自定义方法
                    result = supplier.get();
                    logHitDataBase(redisKey);
                    // 处理缓存穿透
                    if (result == null || (result instanceof Collection && ((Collection) result).size() < 1)) {
                        // 此处不需要以RedisLock形式执行，因为外部的LoadLockKey等同于已经加了同一把逻辑锁；
                        redisUtil.setex(redisKey.getRefreshKey(), DistributedCache.EMPTY_VALUE_FOR_REFRESH, redisKey.getRefreshExpireSeconds());
                        logSetRefresh(redisKey);
                    }
                    return result;
                } else {
                    // 如果超时或者key中已有值，则快速返回；
                    if (existsKeyMulti(redisKey) || System.currentTimeMillis() - startTime >= timeOutMillSec) {
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
