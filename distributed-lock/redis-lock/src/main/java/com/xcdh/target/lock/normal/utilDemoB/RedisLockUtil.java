package com.xcdh.target.lock.normal.utilDemoB;

import com.xcdh.target.lock.normal.abstractClass.AbstractDistributedLock;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.UUID;

/**
 * Project: Wheel
 *
 * Description:
 *
 * @author: MikeLC
 *
 * @date: 2019/3/26 19:46
 **/
public class RedisLockUtil extends AbstractDistributedLock {


    private ThreadLocal<String> lockToken = new ThreadLocal<String>();

    private Jedis jedis;

    public RedisLockUtil(Jedis jedis){
        super();
        this.jedis = jedis;
    }

    @Override
    public boolean setRedis(String lockKey, long expireTime) {
        String uuid = Thread.currentThread().getId() + "_" +UUID.randomUUID().toString().toLowerCase();
        lockToken.set(uuid);

        String result = jedis.set(lockKey, uuid, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean releaseLock(String lockKey) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(lockToken.get()));
        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

}