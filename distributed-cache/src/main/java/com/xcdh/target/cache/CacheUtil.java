package com.xcdh.target.cache;


import com.xcdh.target.lock.normal.constant.RedisKey;
import com.xcdh.target.lock.normal.utilDemoB.RedisLockUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
@Component
public class CacheUtil {

    private static Logger logger = LoggerFactory.getLogger(CacheUtil.class);

    private static final String MULTI_LOAD_LUA = "local existFlag = redis.call('EXISTS',KEYS[1]);" +
            "if (existFlag) then return then redis.call('set',KEYS[1]','','ex',ARGS[1],'NX');";

    @Resource
    protected RedisUtil redisUtil;
    @Autowired
    private RedisLockUtil redisLockUtil;

    /**
     *
     * @param key
     * @return
     */
    public String get(String key) {
        String result = null;
        try {
            result = this.redisUtil.get(key);
        } catch (Exception e) {
            logger.error("CacheUtil_get, key:" + key, e);
        }
        return result;
    }

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
     * 获取hashMap
     */
    public Map<String, String> getHashFromCache(RedisKey key) {
        Map<String, String> result = null;
        try {
            result = this.redisUtil.hgetAll(key.getKey());
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
    public <T> T refreshLockLoad(RedisKey redisKey, Supplier<T> supplier) {
        T result = null;
        long startTime = System.currentTimeMillis();
        try {
            int retryTime = 100;
            while (retryTime-- > 0){
                if (existsRedisKey(redisKey)) {
                    return null;
                }
                if (redisLockUtil.lock(redisKey.getLoadLockKey(), redisKey.getLoadLockExpireSeconds())) {
                    //
                    if (existsRedisKey(redisKey)) {
                        this.delRedisKey(redisKey);
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
                if (existsRedisKey(redisKey)) {
                    return null;
                }
                if (redisLockUtil.lock(redisKey.getLoadLockKey(), redisKey.getLoadLockExpireSeconds())) {
                    // doubleCheck之锁外判断-快速返回
                    if (existsRedisKey(redisKey)) {
                        return null;
                    }
                    // 执行自定义方法
                    result = supplier.get();
                    // 处理缓存穿透
                    if (result == null || (result instanceof Collection && ((Collection) result).size() < 1)) {
                        redisUtil.setex(redisKey.getRefreshLockKey(), "1", redisKey.getRefreshLockExpireSeconds());
                    }
                    return result;
                } else {
                    // 如果超时或者key中已有值，则快速返回；
                    if (existsRedisKey(redisKey) || System.currentTimeMillis() - startTime >= timeOutMillSec) {
                        return null;
                    }
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {}
                }
            }
        } catch (Exception e) {
            logger.error("redis异常", e);
            throw new RuntimeException();
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

    public boolean existsRedisKey(RedisKey key) {
        return redisUtil.exists(key.getKey()) || redisUtil.exists(key.getRefreshLockKey());
    }

    public boolean delRedisKey(RedisKey key){
        redisUtil.del(key.getKey(),key.getRefreshLockKey());
        return true;
    }

/*
    public <T> T cacheLockLoadForce(String lockKey, String checkKey, Supplier<T> supplier) {
        try {
            //这个循环次数应该斟酌一下 超时设置成5秒，超过10秒就不等了，直接异常
            for (int i = 0; i < 100; i++) {
                if (redisLockUtil.lock(lockKey, 5000)) {
                    T result = supplier.get();
                    return result;
                } else {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                    }
                }
            }
        } catch (Exception e) {
            logger.error("redis异常", e);
            throw new RuntimeException("fail");
        } finally {
            redisLockUtil.releaseLock(lockKey);
        }

        logger.error("获取数据库锁失败");
        throw new RuntimeException("fail");
    }

    public void cacheLockLoadMultiple(List<RedisKey> redisKeys, Consumer<List<Long>> consumer) {
        //加锁失败的key
        List<RedisKey> lockFailed = new ArrayList<>(redisKeys.size());
        try {
            List<Long> paramIds = new ArrayList<>(redisKeys.size());

            for (int i = 0; i < redisKeys.size(); i++) {
                RedisKey redisKey = redisKeys.get(i);
                boolean hasKey = redisUtil.exists(redisKey.getKey());
                if (hasKey) {
                    continue;
                } else {
                    try {
                        if (1 == redisUtil.setnx(redisKey.getLoadLockKey(), "1", 5000)) {
                            hasKey = redisUtil.exists(redisKey.getKey());
                            if (hasKey) {
                                //加锁之后如果key已经存在，则放锁
                                continue;
                            } else {
                                paramIds.add(redisKey.getId());
                            }
                        } else {
                            lockFailed.add(redisKey);
                        }
                    } finally {
                        redisUtil.del(redisKey.getLoadLockKey());
                    }
                }
            }
            if (paramIds.size() == 0) {
                return;
            }
            consumer.accept(paramIds);
            //只等1秒，拿不全直接redis写入空字符串返回(lua脚本)
            int i = 0;
            List<RedisKey> luaParams = new ArrayList<>(lockFailed.size());
            for (; i < 50; i++) {
                for (RedisKey redisKey : lockFailed) {
                    boolean hasKey = redisUtil.exists(redisKey.getKey());
                    if (!hasKey) {
                        if (i != 49) {
                            try {
                                Thread.sleep(20);
                                continue;
                            } catch (InterruptedException e) {
                            }
                        } else {
                            luaParams.add(redisKey);
                        }
                    }
                }
            }
            if (luaParams.size() != 0) {
                BPipeline bPipeline = redisUtil.pipeline();
                Pipeline pipeline = bPipeline.pipeline();
                for (RedisKey lua : luaParams) {
                    pipeline.eval(MULTI_LOAD_LUA, Collections.singletonList(lua.getKey()), Collections.singletonList("10"));
                }
                bPipeline.submitAndReturn();
            }
        } catch (Exception e) {
            logger.error("redis异常", e);
            throw new RuntimeException("fail");
        }
    }

*/

}
