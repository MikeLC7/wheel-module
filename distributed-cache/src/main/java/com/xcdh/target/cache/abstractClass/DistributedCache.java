package com.xcdh.target.cache.abstractClass;

import com.xcdh.target.redis.RedisKey;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Project: Wheel
 *
 * Description:
 *
 * @author: MikeLC
 *
 * @date: 2019/3/26 19:14
 **/
public interface DistributedCache {

    //cache 字符串数据主key对应存放的空值；
    public static final String EMPTY_STRING = "null";
    //cache Map数据空值时的key&value指定值；
    public static final String EMPTY_MAP = "mapnull";
    // 缓存防穿透；指定value值
    public static final String EMPTY_VALUE_FOR_REFRESH = "1";

    public static final long CACHE_LOAD_TIMEOUT = 2000;

    /**
     * 获取string 缓存数据
     *
     * @param key
     * @return
     */
    public String getStringFromCache(RedisKey key);

    /**
     * 存储String类型数据
     *
     * @param key
     * @param value
     * @return
     */
    public boolean putStringToCache(RedisKey key, String value, int exp);

    /**
     * 获取hashMap
     */
    public Map<String, String> getHashFromCache(RedisKey key);

    /**
     * 存储hashMap
     */
    public boolean putHashToCache(RedisKey key, Map<String, String> value, int exp);

    /**
     * 同步互斥-删除缓存
     * @param redisKey
     * @param supplier
     * @param <T>
     * @return
     */
    public <T> T refreshLockLoad(RedisKey redisKey, Supplier<T> supplier);

    /**
     * 同步互斥-查询缓存or设置缓存；
     * @param redisKey
     * @param supplier
     * @param timeOutMillSec
     * @param <T>
     * @return
     */
    public <T> T cacheLockLoad(RedisKey redisKey, Supplier<T> supplier, long timeOutMillSec);

    /**
     * 主key是否有值
     * @param key
     * @return
     */
    public boolean existsKey(RedisKey key);

    /**
     * refreshKey是否有值
     * @param key
     * @return
     */
    public boolean existsRefreshKey(RedisKey key);

    /**
     * 主key或refreshKey是否有值
     * @param key
     * @return
     */
    public boolean existsKeyMulti(RedisKey key);

    /**
     * 删除主key
     * @param key
     * @return
     */
    public boolean delKey(RedisKey key);

    /**
     * 删除refreshkey
     * @param key
     * @return
     */
    public boolean delRefreshKey(RedisKey key);

    /**
     * 删除主key&refreshKey
     * @param key
     * @return
     */
    public boolean delKeyMulti(RedisKey key);

    /**
     * 获取“防穿透指定值-字符串”
     * @return
     */
    public String getEmptyString() ;

    /**
     * 判断是否是“防穿透指定值-字符串”
     * @param str
     * @return
     */
    boolean isEmptyString(String str) ;

    /**
     * 获取“防穿透指定值-字符串”
     * @return
     */
    public Map<String, String> getEmptyMap() ;

    /**
     * 判断是否是“防穿透指定值-Map”
     * @param map
     * @return
     */
    boolean isEmptyMap(Map<String, String> map) ;

    /**
     * 打点-命中数据库
     */
    void logHitDataBase(RedisKey redisKey) ;

    /**
     * 打点-设置刷新
     */
    void logSetRefresh(RedisKey redisKey) ;

    /**
     * 打点-命中刷新值
     * @param redisKey
     */
    void logHitRefresh(RedisKey redisKey) ;

}