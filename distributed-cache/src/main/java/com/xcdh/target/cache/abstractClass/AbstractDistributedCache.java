package com.xcdh.target.cache.abstractClass;

import com.xcdh.target.redis.RedisKey;
import com.xcdh.target.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * Project: Wheel
 *
 * Description:
 *
 * @author: MikeLC
 *
 * @date: 2019/3/26 19:14
 **/
public abstract class AbstractDistributedCache implements DistributedCache {

    private static Logger logger = LoggerFactory.getLogger(AbstractDistributedCache.class);

    @Autowired
    protected RedisUtil redisUtil;

    @Override
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

    @Override
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

    @Override
    public boolean existsKey(RedisKey key) {
        return redisUtil.exists(key.getKey());
    }

    @Override
    public boolean existsRefreshKey(RedisKey key) {
        return redisUtil.exists(key.getRefreshKey());
    }

    @Override
    public boolean existsKeyMulti(RedisKey key) {
        return redisUtil.exists(key.getKey()) || redisUtil.exists(key.getRefreshKey());
    }

    @Override
    public boolean delKey(RedisKey key){
        redisUtil.del(key.getKey());
        return true;
    }

    @Override
    public boolean delRefreshKey(RedisKey key){
        redisUtil.del(key.getRefreshKey());
        return true;
    }

    @Override
    public boolean delKeyMulti(RedisKey key){
        redisUtil.del(key.getKey(),key.getRefreshKey());
        return true;
    }

    @Override
    public String getEmptyString() {
        return DistributedCache.EMPTY_STRING;
    }

    @Override
    public boolean isEmptyString(String str) {
        if (StringUtils.isBlank(str)){
            return true;
        }

        if (DistributedCache.EMPTY_STRING.equals(str)){
            return true;
        }

        return false;
    }

    @Override
    public Map<String, String> getEmptyMap() {
        Map<String, String> info = new HashMap<>();
        info.put(DistributedCache.EMPTY_MAP, DistributedCache.EMPTY_MAP);
        return info;
    }

    @Override
    public boolean isEmptyMap(Map<String, String> map) {
        if (null == map){
            return true;
        }

        if (map.size() == 1 && map.containsKey(DistributedCache.EMPTY_MAP)) {
            return true;
        }

        return false;
    }

    @Override
    public void logHitDataBase(RedisKey redisKey) {
        logger.info("logHitDataBase：key={}", redisKey.getKey());
    }

    @Override
    public void logSetRefresh(RedisKey redisKey) {
        logger.info("logSetRefresh：refreshKey={}", redisKey.getRefreshKey());
    }

    @Override
    public void logHitRefresh(RedisKey redisKey) {
        logger.info("logHitRefresh：refreshKey={}", redisKey.getRefreshKey());
    }

}
