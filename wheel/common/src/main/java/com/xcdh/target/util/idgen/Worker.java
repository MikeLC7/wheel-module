package com.xcdh.target.util.idgen;

import com.xcdh.target.redis.service.RedisSentinelService;
import com.xcdh.target.redis.service.RedisSentinelServiceImpl;

public class Worker {

    public static Long workerId = null;

    public synchronized static void initWorkerId(String appUniqName, int clusterId, String sentinelHost, String masterName) throws Exception {
        if (null != workerId)
            return;

        int appId = 999;
        int maxWaitMillis = 1000;
        int maxTotal = 1000;
        int minIdle = 8;
        int maxIdle = 100;
        int timeOut = 1000;

        String appKey = Constent.WORKER_PREFIX + appUniqName;

        try {
            String uniqId = IpUtil.getComputerId();
            if (null == uniqId)
                throw new Exception("get uniq id error");

            RedisSentinelService redis = new RedisSentinelServiceImpl(appId, clusterId, sentinelHost, masterName, maxWaitMillis, maxTotal, minIdle, maxIdle, timeOut);
            String wId = redis.hget(appKey, uniqId);
            if (null != wId) {
                workerId = Long.parseLong(wId);
                System.out.println("current machine workerId:" + workerId);
                return;
            }

            // TODO: RedisLock 与 Util 依赖关系需要调整；
            /*RedisLock redisLock = new RedisLock(appKey, redis);
            try {
                if (redisLock.lock()) {
                    long cn = 0;
                    String currentNum = redis.hget(appKey, "current_num");
                    if (null != currentNum) {
                        cn = Long.parseLong(currentNum) + 1;
                        if (cn >= Constent.WORKERID_MAX_NUM)
                            cn = 0;
                    }

                    redis.hset(appKey, "current_num", String.valueOf(cn));
                    redis.hset(appKey, uniqId, String.valueOf(cn));

                    workerId = cn;
                } else
                    throw new Exception("init workerId redis error");
            } catch (Exception e) {
                throw e;
            } finally {
                redisLock.unlock();
            }*/
        } catch (Exception e) {
            throw new Exception("init workerId redis error ", e);
        }
    }
}
