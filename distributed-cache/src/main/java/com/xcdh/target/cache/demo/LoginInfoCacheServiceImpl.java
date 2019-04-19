package com.xcdh.target.cache.demo;

import com.xcdh.target.cache.abstractClass.DistributedCache;
import com.xcdh.target.redis.RedisKey;
import com.xcdh.target.redis.RedisKeyConst;
import com.xcdh.target.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("loginInfoCacheService")
public class LoginInfoCacheServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(LoginInfoCacheServiceImpl.class);

    @Autowired
    @Qualifier("cacheUtil")
    private DistributedCache distributedCache;

    public Map<String, String> getLoginInfoByUserId(Long userId) {
        RedisKey redisKey = RedisKeyConst.LOGININFO_UID_KEY(userId);
        Map<String, String> resInfo = distributedCache.getHashFromCache(redisKey);
        if (null == resInfo) {
            distributedCache.cacheLockLoad(redisKey, () -> {
                Map<String, String> loginInfo = new HashMap<>();
                // loginInfo = XXXX;
                cacheUserLoginInfo(userId, loginInfo);
                return loginInfo;
            }, DistributedCache.CACHE_LOAD_TIMEOUT);
        }
        return resInfo;
    }

    public Boolean cacheUserLoginInfo(Long userId, Map<String, String> info) {
        RedisKey key = RedisKeyConst.LOGININFO_UID_KEY(userId);
        if (!CollectionUtil.isEmpty(info)) {
            distributedCache.putHashToCache(key, info, RedisKeyConst.USER_LOGIN_TIME_EXP);
        }
        return true;
    }

    public Boolean refreshUserLoginInfo(Long userId) {
        RedisKey redisKey = RedisKeyConst.LOGININFO_UID_KEY(userId);
        distributedCache.refreshLockLoad(redisKey, () -> {
            Map<String, String> loginInfo = new HashMap<>();
            // loginInfo = XXXX;
            cacheUserLoginInfo(userId, loginInfo);
            return loginInfo;
        });
        return true;
    }

    public Boolean deleteUserLoginInfo(Long userId) {
        RedisKey redisKey = RedisKeyConst.LOGININFO_UID_KEY(userId);
        distributedCache.refreshLockLoad(redisKey, () -> {
            logger.info("deleteUserLoginInfo");
            return null;
        });
        return true;
    }


}
