package com.xcdh.target.redis;

import com.alibaba.fastjson.JSON;
import com.xcdh.target.redis.model.BPipeline;
import com.xcdh.target.redis.service.RedisSentinelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

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
public class RedisUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);

    //private RedisSentinelPool redisService;
    private RedisSentinelService redisService;

    public RedisUtil() {
    }
/*

    public RedisUtil(int appId, int clusterId, String sentinelHost, String masterName, int maxWaitMillis,
                     int maxTotal, int minIdle, int maxIdle, int timeOut) throws Exception {
        redisService = new RedisSentinelPool(appId, clusterId, sentinelHost, masterName,
                maxWaitMillis, maxTotal, minIdle, maxIdle, timeOut);
    }
*/

    /**
     * 生成以businessId为前缀的19位ID，businessId + timestamp + 10000以内Redis序列号
     *
     * @param businessId
     * @return
     */
    public Long generateId(int businessId) {
        final String INCR_KEY = "cms:generate_id:";
        final int EXPIRE_SECONDS = 10;
        Jedis jedis = redisService.getJedis();
        if (businessId > 91) {
            throw new RuntimeException("businessId不能大于91");
        }
        try {
            List<String> timeStr = jedis.time();
            long ms = Long.valueOf(timeStr.get(0) + timeStr.get(1).substring(0, 3));
            long serialNum = jedis.incr(INCR_KEY + ms);
            jedis.expire(INCR_KEY + ms, EXPIRE_SECONDS);
            return businessId * 100000000000000000L + ms * 10000L + serialNum;
        } catch (Exception je) {
            // 异常降级本地生成（businessId + 毫秒 + 随机数）
            //return businessId* 100000000000000000L + System.currentTimeMillis() * 10000L + (int)(Math.random() * 10000);
            // 降级1/10000概率导致重复，导致数据混乱，抛出异常
            throw new RuntimeException("ID生成器，基于Redis生成序列号失败");
        } finally {
            jedis.close();
        }
    }

    public boolean exists(String key) {
        try {
            return redisService.exists(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean notExists(String key) {
        return !exists(key);
    }

    public String get(String key) {
        try {
            return redisService.get(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String set(String key, String value) {
        try {
            return redisService.set(key, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long del(String key) {
        try {
            return redisService.del(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long del(String... keys) {
        try {
            return redisService.del(keys);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取key 的值， 并支持读取续期
     *
     * @param key
     * @param resetTtl
     * @return
     */
    public String get(String key, int resetTtl) {
        BPipeline bPipeline = this.pipeline();
        Pipeline pipeline = bPipeline.pipeline();
        Response<String> resp = pipeline.get(key);
        pipeline.expire(key, resetTtl);
        bPipeline.submitAndReturn();
        return resp.get();
    }

    /**
     * 批量读取key的值【get】，并支持读取续期
     *
     * @param keys
     * @param resetTtl
     * @return
     */
    public List<String> getList(List<String> keys, Integer resetTtl) {
        if (CollectionUtils.isEmpty(keys)) {
            return new ArrayList<>();
        }
        BPipeline bPipeline = this.pipeline();
        Pipeline pipeline = bPipeline.pipeline();
        List<Response<String>> respList = new ArrayList<>();
        for (String key : keys) {
            respList.add(pipeline.get(key));
            if (resetTtl != null) {
                pipeline.expire(key, resetTtl);
            }
        }
        bPipeline.submitAndReturn();
        return respList.stream().map(r -> r.get()).collect(Collectors.toList());
    }

    /**
     * 设置并覆盖原有的过期时间
     *
     * @param key
     * @param value
     * @param expireTime
     * @return
     */
    public String setex(String key, String value, int expireTime) {
        try {
            return redisService.setex(key, expireTime, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setex(String key, String value, Date expireDate) {

        try {
            BPipeline bPipeline = this.pipeline();
            Pipeline pipeline = bPipeline.pipeline();
            pipeline.set(key, value);
            pipeline.expireAt(key, expireDate.getTime() / 1000);
            bPipeline.submitAndReturn();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param key
     * @param value
     * @param nxxx       NX|XX,
     *                   <br>NX -- Only set the key if it does not already exist.
     *                   <br>XX -- Only set the key if it already exist.
     *                   <br>
     * @param expx       EX|PX, expire time units:
     *                   <br>EX = seconds;
     *                   <br>PX = milliseconds
     * @param expireTime
     * @return
     * @throws Exception
     */
    public String set(String key, String value, String nxxx, String expx, long expireTime) {
        try {
            return redisService.set(key, value, nxxx, expx, expireTime);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long setnx(String key, String value) {
        try {
            return redisService.setnx(key, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public boolean getLock(String key, int expireSeconds) {
        return 1L == setnx(key, "1", expireSeconds);
    }

    public void releaseLock(String key) {
        expire(key, -2);
    }

    /**
     * key不存在时，可以写入，并设置有效期
     * key存在时，写入失败
     *
     * @param key
     * @param value
     * @param expireTime
     * @return
     */
    public Long setnx(String key, String value, int expireTime) {
        String result = null;
        try {
            result = redisService.set(key, value, "NX", "EX", expireTime);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result == null ? 0L : 1L;
    }

    public Long expire(String key, int expireTime) {
        try {
            return redisService.expire(key, expireTime);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String rename(String oldKeyName, String newKeyName) {
        try {
            return redisService.rename(oldKeyName, newKeyName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Set<String> hkeys(String key) {
        try {
            return redisService.hkeys(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hexists(String key, String field) {
        try {
            return redisService.hexists(key, field);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long hdel(String key, String... field) {
        try {
            return redisService.hdel(key, field);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> hvals(String key) {
        try {
            return redisService.hvals(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long hset(String key, String field, String value) {
        try {
            return redisService.hset(key, field, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long hset(String key, String field, String value, int expireTime) {
        try {
            BPipeline bPipeline = redisService.pipeline();
            Pipeline pipeline = bPipeline.pipeline();
            Response<Long> result = pipeline.hset(key, field, value);
            pipeline.expire(key, expireTime);
            bPipeline.submitAndReturn();
            return result.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long hsetnx(String key, String field, String value) {
        try {
            return redisService.hsetnx(key, field, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long hincrBy(String key, String field, long value) {
        try {
            return redisService.hincrBy(key, field, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param key
     * @param field
     * @param value
     * @param expireSeconds 剩余过期时间 单位秒
     * @return
     */
    public Long hincrBy(String key, String field, long value, int expireSeconds) {
        try {
            BPipeline bPipeline = this.pipeline();
            Pipeline pipeline = bPipeline.pipeline();
            Response<Long> result = pipeline.hincrBy(key, field, value);
            pipeline.expire(key, expireSeconds);
            bPipeline.submitAndReturn();
            return result.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param key
     * @param field
     * @param value
     * @param expireAtSeconds 过期时间戳，单位秒
     * @return
     */
    public Long hincrBy(String key, String field, long value, long expireAtSeconds) {
        try {
            BPipeline bPipeline = this.pipeline();
            Pipeline pipeline = bPipeline.pipeline();
            Response<Long> result = pipeline.hincrBy(key, field, value);
            pipeline.expireAt(key, expireAtSeconds);//秒数
            bPipeline.submitAndReturn();
            return result.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long incrBy(String key, long value, Date expireDate) {
        try {
            BPipeline bPipeline = this.pipeline();
            Pipeline pipeline = bPipeline.pipeline();
            Response<Long> result = pipeline.incrBy(key, value);
            pipeline.expireAt(key, expireDate.getTime() / 1000);//秒数
            bPipeline.submitAndReturn();
            return result.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long decrBy(String key, long value) {
        try {
            BPipeline bPipeline = this.pipeline();
            Pipeline pipeline = bPipeline.pipeline();
            Response<Long> result = pipeline.decrBy(key, value);
            bPipeline.submitAndReturn();
            return result.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long decrBy(String key, long value, int expireSeconds) {
        try {
            BPipeline bPipeline = this.pipeline();
            Pipeline pipeline = bPipeline.pipeline();
            Response<Long> result = pipeline.decrBy(key, value);
            pipeline.expire(key, expireSeconds);//秒数
            bPipeline.submitAndReturn();
            return result.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long decrBy(String key, long value, Date expireDate) {
        try {
            BPipeline bPipeline = this.pipeline();
            Pipeline pipeline = bPipeline.pipeline();
            Response<Long> result = pipeline.decrBy(key, value);
            pipeline.expireAt(key, expireDate.getTime() / 1000);//秒数
            bPipeline.submitAndReturn();
            return result.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String hget(String key, String field) {
        try {
            return redisService.hget(key, field);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, String> hgetAll(String key) {
        try {
            return redisService.hgetAll(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> hmget(String key, String... field) {
        try {
            return redisService.hmget(key, field);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String hmset(String key, Map<String, String> map) {
        try {
            return redisService.hmset(key, map);
        } catch (Exception e) {
            LOGGER.error(JSON.toJSONString(map));
            throw new RuntimeException(e);
        }
    }

    public List<Object> hmsetAfterDel(String key, int seconds, Map<String, String> hash) throws JedisException {
        try {
            return redisService.hmsetAfterDel(key, seconds, hash);
        } catch (Exception e) {
            LOGGER.error(JSON.toJSONString(hash));
            throw new RuntimeException(e);
        }
    }

    public Long hlen(String key) {
        try {
            return redisService.hlen(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long incr(String key) {
        try {
            return redisService.incr(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long incrBy(String key, Long l) {
        try {
            return redisService.incrBy(key, l);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long incr(String key, int expireTime) {
        BPipeline pipeline = null;
        try {
            pipeline = redisService.pipeline();
            Response<Long> result = pipeline.pipeline().incr(key);
            pipeline.pipeline().expire(key, expireTime);
            pipeline.submitAndReturn();
            return result.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public Long decr(String key) {
        try {
            return redisService.decr(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long decr(String key, int expireTime) {
        BPipeline pipeline = null;
        try {
            pipeline = redisService.pipeline();
            Response<Long> result = pipeline.pipeline().decr(key);
            pipeline.pipeline().expire(key, expireTime);
            pipeline.submitAndReturn();
            return result.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public int ttl(String key) {
        try {
            return redisService.ttl(key).intValue();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long llen(String key) {
        try {
            return redisService.llen(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> lrange(String key, long start, long end) {
        try {
            return redisService.lrange(key, start, end);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long zadd(String key, String member, double value) {
        try {
            return redisService.zadd(key, value, member);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long zrem(String key, String member) {
        try {
            return redisService.zrem(key, member);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Double zscore(String key, String member) {
        try {
            return redisService.zscore(key, member);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long zcard(String key) {
        try {
            return redisService.zcard(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long zremrange(String key, String... members) {
        try {
            return redisService.zrem(key, members);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long zremrangeByScore(String key, String min, String max) {
        try {
            return redisService.zremrangeByScore(key, min, max);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long zremrangeByScore(String key, long min, long max) {
        try {
            return redisService.zremrangeByScore(key, min, max);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long zcount(String key, int min, long max) {
        try {
            return redisService.zcount(key, min, max);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Set<String> zrange(String key, Long start, Long end) {
        try {
            return redisService.zrange(key, start, end);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Set<Tuple> zrevrangeWithScores(String key, long start, long stop) {
        try {
            return redisService.zrevrangeWithScores(key, start, stop);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Set<String> zrevrangeByScore(String key, String start, String end) {
        try {
            return redisService.zrevrangeByScore(key, start, end);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Set<Tuple> zrevrangeByScoreWithScores(String key, String start, String end) {
        try {
            return redisService.zrevrangeByScoreWithScores(key, start, end);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public BPipeline pipeline() {
        try {
            return redisService.pipeline();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Integer> batchZank(String key, List<String> members) {
        Map<String, Integer> res = new HashMap<String, Integer>();
        Jedis jedis = null;
        try {
            jedis = redisService.getJedis();
            Pipeline p = jedis.pipelined();
            for (String id : members) {
                p.zrevrank(key, id);
            }

            List<Object> resObj = p.syncAndReturnAll();
            for (int i = 0; i < resObj.size(); i++) {
                Object obj = resObj.get(i);
                if (null == obj) {
                    continue;
                }
                res.put(members.get(i), Integer.parseInt(obj.toString()) + 1);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

        return res;
    }

    public Set<Long> getTopN(String key, int start, int stop) {
        Set<Long> res = new HashSet<Long>();
        try {
            for (String id : redisService.zrevrange(key, start, stop)) {
                res.add(Long.parseLong(id));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return res;
    }

    public Long getMyRank(String key, int id) {
        try {
            return redisService.zrank(key, String.valueOf(id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String scriptLoad(String scriptText) {
        try {
            return redisService.scriptLoad(scriptText);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object evalsha(String script, List<String> keyList, List<String> argList) {
        try {
            return redisService.evalsha(script, keyList, argList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object eval(String script, List<String> keyList, List<String> argList) {
        try {
            return redisService.eval(script, keyList, argList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 渐进删除大Hash
     */
    public void delBigHash(String bigHashKey) {
        Jedis jedis = redisService.getJedis();
        try {
            ScanParams scanParams = new ScanParams().count(100);
            String cursor = "0";
            do {
                ScanResult<Map.Entry<String, String>> scanResult = jedis.hscan(bigHashKey, cursor, scanParams);
                List<Map.Entry<String, String>> entryList = scanResult.getResult();
                if (entryList != null && !entryList.isEmpty()) {
                    for (Map.Entry<String, String> entry : entryList) {
                        jedis.hdel(bigHashKey, entry.getKey());
                    }
                }
                cursor = scanResult.getStringCursor();
            } while (!"0".equals(cursor));
            //删除bigkey
            jedis.del(bigHashKey);
        } finally {
            jedis.close();
        }
    }

    /**
     * 渐进删除大list
     */
    public void delBigList(String bigListKey) {
        Jedis jedis = redisService.getJedis();
        try {
            long llen = jedis.llen(bigListKey);
            int counter = 0;
            int left = 100;
            while (counter < llen) {
                //每次从左侧截掉100个
                jedis.ltrim(bigListKey, left, llen);
                counter += left;
            }
            //最终删除key
            jedis.del(bigListKey);
        } finally {
            jedis.close();
        }
    }

    /**
     * 渐进删除大SET
     */
    public void delBigSet(String bigSetKey) {
        Jedis jedis = redisService.getJedis();
        try {
            ScanParams scanParams = new ScanParams().count(100);
            String cursor = "0";
            do {
                ScanResult<String> scanResult = jedis.sscan(bigSetKey, cursor, scanParams);
                List<String> memberList = scanResult.getResult();
                if (memberList != null && !memberList.isEmpty()) {
                    for (String member : memberList) {
                        jedis.srem(bigSetKey, member);
                    }
                }
                cursor = scanResult.getStringCursor();
            } while (!"0".equals(cursor));
            //最终删除bigkey
            jedis.del(bigSetKey);
        } finally {
            jedis.close();
        }
    }

    /**
     * 渐进删除大ZSET
     */
    public void delBigZset(String bigZsetKey) {
        Jedis jedis = redisService.getJedis();
        try {
            ScanParams scanParams = new ScanParams().count(100);
            String cursor = "0";
            do {
                ScanResult scanResult = jedis.zscan(bigZsetKey, cursor, scanParams);
                List<Tuple> tupleList = scanResult.getResult();
                if (tupleList != null && !tupleList.isEmpty()) {
                    for (Tuple tuple : tupleList) {
                        jedis.zrem(bigZsetKey, tuple.getElement());
                    }
                }
                cursor = scanResult.getStringCursor();
            } while (!"0".equals(cursor));
            //删除bigkey
            jedis.del(bigZsetKey);
        } finally {
            jedis.close();
        }
    }

    /**
     * 读取资源文件的内容
     *
     * @param uri 资源路径
     * @return 返回文件内容
     */
    public String readResource(String uri) throws IOException {
        StringBuilder result = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(uri)));
            String s;
            while ((s = br.readLine()) != null) {
                result.append(System.lineSeparator() + s);
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return result.toString();
    }

    public Long lpush(String key, String... vals) {
        try {
            return redisService.lpush(key, vals);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取sortset 的所有成员
     *
     * @param key
     * @return
     */
    public Set<String> smembers(String key) {
        try {
            return redisService.smembers(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param key
     * @param members
     * @return
     */
    public Long srem(String key, String... members) {
        try {
            return redisService.srem(key, members);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param key
     * @return
     */
    public List<String> mget(String... key) {
        try {
            return redisService.mget(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Set<Tuple> zrangeWithScores(String key, long start, long stop) {
        try {
            return redisService.zrangeWithScores(key, start, stop);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
