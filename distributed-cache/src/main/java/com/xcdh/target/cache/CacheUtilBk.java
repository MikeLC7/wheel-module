package com.xcdh.target.cache;

import com.alibaba.fastjson.JSONObject;
import com.xcdh.target.lock.normal.constant.RedisKey;
import com.xcdh.target.lock.normal.utilDemoB.RedisLockUtil;
import com.xcdh.target.redis.RedisUtil;
import com.xcdh.target.util.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
@Component
public class CacheUtilBk {

    private static Logger logger = LoggerFactory.getLogger(CacheUtilBk.class);

    private static final String MULTI_LOAD_LUA = "local existFlag = redis.call('EXISTS',KEYS[1]);" +
            "if (existFlag) then return then redis.call('set',KEYS[1]','','ex',ARGS[1],'NX');";

    public static final long CACHE_LOAD_TIMEOUT = 2000;

    //cache 字符串数据主key对应存放的空值；
    public static final String EMPTY_STRING = "null";
    //cache Map数据空值时的key&value指定值；
    public static final String EMPTY_MAP = "mapnull";
    // 缓存防穿透；指定value值
    public static final String EMPTY_VALUE_FOR_REFRESH = "1";

    @Resource
    protected RedisUtil redisUtil;
    @Autowired
    private RedisLockUtil redisLockUtil;

    /**
     * 获取string 缓存数据
     *
     * @param key
     * @return
     */
    public String getStringFromCache(RedisKey key) {
        String result = null;
        try {
            result = this.redisUtil.get(key.getKey());
            if (StringUtils.isNotBlank(result) && EMPTY_STRING.equals(result)){
                if (logger.isInfoEnabled()) {
                    logger.info("查询String命中Refresh：{}", JSONObject.toJSON(key));
                }
                return null;
            }
        } catch (Exception e) {
            logger.error("getStringFromCache, key:" + key.getKey(), e);
        }
        return result;
    }

    /**
     * 存储String类型数据
     *
     * @param key
     * @param value
     * @return
     */
    public boolean putStringToCache(RedisKey key, String value, int exp) {
        boolean isOk = true;
        try {
            if (exp > 0) {
                this.redisUtil.setex(key.getKey(), value, exp);
            } else {
                this.redisUtil.set(key.getKey(), value);
            }
        } catch (Exception e) {
            logger.error("putStringToCache, key:" + key.getKey(), e);
            isOk = false;
        }
        return isOk;
    }

    /**
     * 获取hashMap
     */
    public Map<String, String> getHashFromCache(RedisKey key) {
        Map<String, String> result = null;
        try {
            result = this.redisUtil.hgetAll(key.getKey());
            if (!CollectionUtil.isEmpty(result) && !hasMapHit(result)){
                if (logger.isInfoEnabled()) {
                    logger.info("查询Map命中Refresh：{}", JSONObject.toJSON(key));
                }
            }
        } catch (Exception e) {
            logger.error("getHashFromCache, key:" + key.getKey(), e);
        }
        return result;
    }

    boolean hasMapHit(Map<String, String> map) {
        if (map.size() == 1 && map.containsKey(EMPTY_MAP)) {
            return false;
        }
        return true;
    }

    /**
     * 存储hashMap
     */
    public boolean putHashToCache(RedisKey key, Map<String, String> value, int exp) {
        boolean isOk = true;
        try {
            if (exp > 0) {
                this.redisUtil.hmsetAfterDel(key.getKey(), exp, value);
            } else {
                this.redisUtil.hmset(key.getKey(), value);
            }
        } catch (Exception e) {
            logger.error("putStringToCache, key:" + key.getKey(), e);
            isOk = false;
        }
        return isOk;
    }

    /**
     * 刷新（删除）缓存
     * @param redisKey
     * @param supplier
     * @param <T>
     * @return
     */
    public <T> T refreshLockLoad(RedisKey redisKey, Supplier<T> supplier) {
        try {
            int retryTime = 100;
            while (retryTime-- > 0){
                if (redisLockUtil.lock(redisKey.getLoadLockKey(), redisKey.getLoadLockExpireSeconds())) {
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
            redisLockUtil.releaseLock(redisKey.getLoadLockKey());
        }
        logger.error("获取数据库锁失败");
        throw new RuntimeException("fail");
    }

    /**
     * 防止缓存雪崩，toc接口读取数据之前先通过获取锁的方式保证只有一次数据库操作
     * 返回结果有可能为null
     * 不能完全依赖返回结果进行后边逻辑
     * 结果返回null的情况，逻辑应该继续从redis中进行
     * 结果不为null的情况，则表示是第一个进行数据库操作的请求，可以依赖返回结果进行后续逻辑，
     * 但最好还会通过redis来进行逻辑，因为依赖返回结果，代码会变的太多
     *
     * @param supplier
     * @param <T>
     * @throws Exception
     */
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
                if (redisLockUtil.lock(redisKey.getLoadLockKey(), redisKey.getLoadLockExpireSeconds())) {
                    // doubleCheck之锁外判断-快速返回
                    if (existsKey(redisKey)) {
                        return null;
                    }
                    // 执行自定义方法
                    return supplier.get();
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
            redisLockUtil.releaseLock(redisKey.getLoadLockKey());
        }
        logger.error("获取数据库锁失败");
        throw new RuntimeException("fail");
    }

    public boolean exLock(RedisKey key) {
        return 1 == redisUtil.setnx(key.getKey(), System.currentTimeMillis() + "", key.getExpireSeconds());
    }

    public void exUnlock(RedisKey key) {
        redisUtil.del(key.getKey());
    }

    public boolean existsRefreshKey(RedisKey key) {
        return redisUtil.exists(key.getRefreshLockKey());
    }

    public boolean existsKey(RedisKey key) {
        return redisUtil.exists(key.getKey());
    }

    public boolean existsKeyMulti(RedisKey key) {
        return redisUtil.exists(key.getKey()) || redisUtil.exists(key.getRefreshLockKey());
    }

    public boolean delKey(RedisKey key){
        redisUtil.del(key.getKey());
        return true;
    }

    public boolean delKeyMulti(RedisKey key){
        redisUtil.del(key.getKey(),key.getRefreshLockKey());
        return true;
    }

}
