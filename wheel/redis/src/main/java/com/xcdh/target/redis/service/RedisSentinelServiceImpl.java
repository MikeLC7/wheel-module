package com.xcdh.target.redis.service;

import com.xcdh.target.redis.config.RedisConfig;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;

import java.util.*;

/**
 * Created by MikeLC
 * Create Date: 2019/4/15 17:34
 * Description: ${DESCRIPTION}
 */
public class RedisSentinelServiceImpl implements RedisSentinelService {


    private int appId;
    private int clusterId;

    public RedisSentinelServiceImpl(int appId, int clusterId, String sentinelHost, String masterName, int maxWaitMillis, int maxTotal, int minIdle, int maxIdle, int timeOut) throws Exception {
        if (appId == 0)
            throw new Exception("init RedisSentinelService error because appId is 0");

        if (clusterId == 0)
            throw new Exception("init RedisSentinelService error because clusterId is 0");

        if (null == sentinelHost || sentinelHost.isEmpty())
            throw new Exception("init RedisSentinelService error because sentinelHost is null");

        if (null == masterName || masterName.isEmpty())
            throw new Exception("init RedisSentinelService error because masterName is null");

        try {
            this.appId = appId;
            this.clusterId = clusterId;
            RedisConfig config = new RedisConfig(maxWaitMillis, maxTotal, minIdle, maxIdle, timeOut);
            this.jedisSentinelPool = JedisFactory.getJedisPoolRetry(getHost(sentinelHost), masterName, config, 5);
        } catch (Exception e) {
            throw e;
        }

        ActionRecordSender.getInstance();
    }

    @Override
    public String set(String var1, String var2) throws JedisException {
        Jedis jedis = getJedis();
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        try {
            return jedis.set(var1, var2);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "set", var1);
        }
    }

    @Override
    public String set(byte[] var1, byte[] var2) throws JedisException {
        Jedis jedis = getJedis();
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        try {
            return jedis.set(var1, var2);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "set", var1);
        }
    }

    @Override
    public String get(String var1) throws JedisException {
        Jedis jedis = getJedis();
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        try {
            String res = jedis.get(var1);
            if (null == res)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "get", var1);
        }
    }

    @Override
    public byte[] get(byte[] var1) throws JedisException {
        Jedis jedis = getJedis();
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        try {
            byte[] res = jedis.get(var1);
            if (null == res)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "get", var1);
        }
    }

    @Override
    public String getSet(String var1, String var2) throws JedisException {
        Jedis jedis = getJedis();
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        try {
            String res = jedis.getSet(var1, var2);
            if (null == res)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "getSet", var1);
        }
    }

    @Override
    public Long setnx(String key, String value) throws JedisException {
        Jedis jedis = getJedis();
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        try {
            return jedis.setnx(key, value);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "setnx", key);
        }
    }

    @Override
    public Long setnx(byte[] key, byte[] value) throws JedisException {
        Jedis jedis = getJedis();
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        try {
            return jedis.setnx(key, value);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "setnx", key);
        }
    }

    @Override
    public Long del(String var1) throws JedisException {
        Jedis jedis = getJedis();
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        try {
            return jedis.del(var1);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "del", var1);
        }
    }

    @Override
    public Long del(byte[] var1) throws JedisException {
        Jedis jedis = getJedis();
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        try {
            return jedis.del(var1);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "del", var1);
        }
    }

    @Override
    public String set(String key, String value, String nxxx, String expx, long time) throws JedisException {
        Jedis jedis = getJedis();
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        try {
            return jedis.set(key, value, nxxx, expx, time);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "set", key);
        }
    }

    @Override
    public String set(byte[] key, byte[] value, byte[] nxxx, byte[] expx, long time) throws JedisException {
        Jedis jedis = getJedis();
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        try {
            return jedis.set(key, value, nxxx, expx, time);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "set", key);
        }
    }

    @Override
    public Long exists(String... keys) throws JedisException {
        Jedis jedis = getJedis();
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        try {
            return jedis.exists(keys);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "exists", keys);
        }
    }

    @Override
    public Boolean exists(String key) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.exists(key);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "exists", key);
        }
    }

    @Override
    public Long del(String... keys) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.del(keys);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "del", keys);
        }
    }

    @Override
    public String type(String key) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.type(key);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "type", key);
        }
    }

    @Override
    public Set<String> keys(String pattern) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Set<String> res = jedis.keys(pattern);
            if (null == res)
                returnNull = true;

            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "keys", pattern);
        }
    }

    @Override
    public String randomKey() throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            String res = jedis.randomKey();
            if (null == res)
                returnNull = true;

            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "randomKey", "[randomKey]");
        }
    }

    @Override
    public String rename(String oldkey, String newkey) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.rename(oldkey, newkey);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "rename", oldkey, newkey);
        }
    }

    @Override
    public Long renamenx(String oldkey, String newkey) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.renamenx(oldkey, newkey);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "renamenx", oldkey, newkey);
        }
    }

    @Override
    public Long expire(String key, int seconds) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.expire(key, seconds);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "expire", key);
        }
    }

    @Override
    public Long expire(byte[] key, int seconds) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.expire(key, seconds);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "expire", key);
        }
    }

    @Override
    public Long expireAt(String key, long unixTime) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.expireAt(key, unixTime);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "expireAt", key);
        }
    }

    @Override
    public Long expireAt(byte[] key, long unixTime) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.expireAt(key, unixTime);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "expireAt", key);
        }
    }

    @Override
    public Long ttl(String key) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.ttl(key);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "ttl", key);
        }
    }

    @Override
    public Long ttl(byte[] key) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.ttl(key);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "ttl", key);
        }
    }

    @Override
    public List<String> mget(String... keys) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            List<String> res = jedis.mget(keys);
            if (null == res)
                returnNull = true;

            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "mget", keys);
        }
    }

    @Override
    public List<byte[]> mget(byte[]... keys) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            List<byte[]> res = jedis.mget(keys);
            if (null == res)
                returnNull = true;

            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "mget", keys);
        }
    }

    @Override
    public String setex(String key, int seconds, String value) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.setex(key, seconds, value);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "setex", key);
        }
    }

    @Override
    public String setex(final byte[] key, final int seconds, final byte[] value) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.setex(key, seconds, value);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "setex", key);
        }
    }

    @Override
    public String mset(String... keysvalues) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.mset(keysvalues);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "mset", keysvalues);
        }
    }

    @Override
    public String mset(byte[]... keysvalues) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.mset(keysvalues);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "mset", keysvalues);
        }
    }

    @Override
    public Long msetnx(String... keysvalues) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.msetnx(keysvalues);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "msetnx", keysvalues);
        }
    }

    @Override
    public Long decrBy(String key, long integer) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.decrBy(key, integer);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "decrBy", key);
        }
    }

    @Override
    public Long decr(String key) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.decr(key);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "decr", key);
        }
    }

    @Override
    public Long incrBy(String key, long integer) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.incrBy(key, integer);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "incrBy", key);
        }
    }

    @Override
    public Double incrByFloat(String key, double value) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.incrByFloat(key, value);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "incrByFloat", key);
        }
    }

    @Override
    public Long incr(String key) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.incr(key);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "incr", key);
        }
    }

    @Override
    public Long append(String key, String value) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.append(key, value);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "append", key);
        }
    }

    @Override
    public String substr(String key, int start, int end) throws JedisException {
        long s = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.substr(key, start, end);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(s, exception, returnNull, "substr", key);
        }
    }

    @Override
    public Long hset(String key, String field, String value) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.hset(key, field, value);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "hset", key);
        }
    }

    @Override
    public Long hset(byte[] key, byte[] field, byte[] value) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.hset(key, field, value);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "hset", key);
        }
    }

    @Override
    public String hget(String key, String field) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            String res = jedis.hget(key, field);
            if (null == res)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "hget", key);
        }
    }

    @Override
    public byte[] hget(byte[] key, byte[] field) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            byte[] res = jedis.hget(key, field);
            if (null == res)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "hget", key);
        }
    }

    @Override
    public Long hsetnx(String key, String field, String value) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.hsetnx(key, field, value);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "hsetnx", key);
        }
    }

    @Override
    public Long hsetnx(final byte[] key, final byte[] field, final byte[] value) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.hsetnx(key, field, value);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "hsetnx", key);
        }
    }

    @Override
    public String hmset(String key, Map<String, String> hash) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.hmset(key, hash);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "hmset", key);
        }
    }

    @Override
    public String hmset(final byte[] key, final Map<byte[], byte[]> hash) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.hmset(key, hash);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "hmset", key);
        }
    }

    @Override
    public List<String> hmget(String key, String... fields) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            List<String> res = jedis.hmget(key, fields);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "hmget", key);
        }
    }

    @Override
    public List<byte[]> hmget(byte[] key, byte[]... fields) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            List<byte[]> res = jedis.hmget(key, fields);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "hmget", key);
        }
    }

    @Override
    public Long hincrBy(String key, String field, long value) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.hincrBy(key, field, value);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "hincrBy", key);
        }
    }

    @Override
    public Double hincrByFloat(String key, String field, double value) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.hincrByFloat(key, field, value);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "hincrByFloat", key);
        }
    }

    @Override
    public Boolean hexists(String key, String field) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.hexists(key, field);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "hexists", key);
        }
    }

    @Override
    public Long hdel(String key, String... fields) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.hdel(key, fields);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "hdel", key);
        }
    }

    @Override
    public Long hdel(byte[] key, byte[]... fields) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.hdel(key, fields);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "hdel", key);
        }
    }

    @Override
    public Long hlen(String key) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.hlen(key);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "hlen", key);
        }
    }

    @Override
    public Set<String> hkeys(String key) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Set<String> res = jedis.hkeys(key);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "hkeys", key);
        }
    }

    @Override
    public List<String> hvals(String key) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            List<String> res = jedis.hvals(key);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "hvals", key);
        }
    }

    @Override
    public Map<String, String> hgetAll(String key) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Map<String, String> res = jedis.hgetAll(key);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "hgetAll", key);
        }
    }

    @Override
    public Map<byte[], byte[]> hgetAll(byte[] key) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Map<byte[], byte[]> res = jedis.hgetAll(key);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "hgetAll", key);
        }
    }

    @Override
    public Long rpush(String key, String... strings) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.rpush(key, strings);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "rpush", key);
        }
    }

    @Override
    public Long rpush(byte[] key, byte[]... strings) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.rpush(key, strings);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "rpush", key);
        }
    }

    @Override
    public Long lpush(String key, String... strings) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.lpush(key, strings);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "lpush", key);
        }
    }

    @Override
    public Long lpush(byte[] key, byte[]... strings) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.lpush(key, strings);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "lpush", key);
        }
    }


    @Override
    public Long llen(String key) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.llen(key);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "llen", key);
        }
    }

    @Override
    public List<String> lrange(String key, long start, long end) throws JedisException {
        long s = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            List<String> res = jedis.lrange(key, start, end);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(s, exception, returnNull, "lrange", key);
        }
    }

    @Override
    public String ltrim(String key, long start, long end) throws JedisException {
        long s = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.ltrim(key, start, end);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(s, exception, returnNull, "ltrim", key);
        }
    }

    @Override
    public String lindex(String key, long index) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.lindex(key, index);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "lindex", key);
        }
    }

    @Override
    public String lset(String key, long index, String value) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.lset(key, index, value);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "lset", key);
        }
    }

    @Override
    public Long lrem(String key, long count, String value) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.lrem(key, count, value);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "lrem", key);
        }
    }

    @Override
    public String lpop(String key) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            String res = jedis.lpop(key);
            if (null == res)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "lpop", key);
        }
    }

    @Override
    public byte[] lpop(byte[] key) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            byte[] res = jedis.lpop(key);
            if (null == res)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "lpop", key);
        }
    }

    @Override
    public String rpop(String key) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            String res = jedis.rpop(key);
            if (null == res)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "rpop", key);
        }
    }

    @Override
    public byte[] rpop(byte[] key) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            byte[] res = jedis.rpop(key);
            if (null == res)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "rpop", key);
        }
    }

    @Override
    public String rpoplpush(String srckey, String dstkey) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            String res = jedis.rpoplpush(srckey, dstkey);
            if (null == res)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "rpoplpush", srckey, dstkey);
        }
    }

    @Override
    public Long sadd(String key, String... members) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.sadd(key, members);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "sadd", key);
        }
    }

    @Override
    public Set<String> smembers(String key) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Set<String> res = jedis.smembers(key);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "smembers", key);
        }
    }

    @Override
    public Long srem(String key, String... members) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.srem(key, members);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "srem", key);
        }
    }

    @Override
    public String spop(String key) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            String res = jedis.spop(key);
            if (null == res)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "spop", key);
        }
    }

    @Override
    public Set<String> spop(String key, long count) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Set<String> res = jedis.spop(key, count);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "spop", key);
        }
    }

    @Override
    public Long smove(String srckey, String dstkey, String member) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.smove(srckey, dstkey, member);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "smove", srckey, dstkey);
        }
    }

    @Override
    public Long scard(String key) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.scard(key);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "scard", key);
        }
    }

    @Override
    public Boolean sismember(String key, String member) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.sismember(key, member);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "sismember", key);
        }
    }

    @Override
    public Set<String> sinter(String... keys) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Set<String> res = jedis.sinter(keys);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "sinter", keys);
        }
    }

    @Override
    public Long sinterstore(String dstkey, String... keys) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.sinterstore(dstkey, keys);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "sinterstore", keys);
        }
    }

    @Override
    public Set<String> sunion(String... keys) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Set<String> res = jedis.sunion(keys);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "sunion", keys);
        }
    }

    @Override
    public Long sunionstore(String dstkey, String... keys) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.sunionstore(dstkey, keys);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "sunionstore", keys);
        }
    }

    @Override
    public Set<String> sdiff(String... keys) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Set<String> res = jedis.sdiff(keys);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "sdiff", keys);
        }
    }

    @Override
    public Long sdiffstore(String dstkey, String... keys) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.sdiffstore(dstkey, keys);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "sdiffstore", keys);
        }
    }

    @Override
    public String srandmember(String key) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            String res = jedis.srandmember(key);
            if (null == res)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "srandmember", key);
        }
    }

    @Override
    public List<String> srandmember(String key, int count) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            List<String> res = jedis.srandmember(key, count);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "srandmember", key);
        }
    }

    @Override
    public Long zadd(String key, double score, String member) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.zadd(key, score, member);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zadd", key);
        }
    }

    @Override
    public Long zadd(String key, double score, String member, ZAddParams params) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.zadd(key, score, member, params);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zadd", key);
        }
    }

    @Override
    public Long zadd(String key, Map<String, Double> scoreMembers) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.zadd(key, scoreMembers);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zadd", key);
        }
    }

    @Override
    public Long zadd(String key, Map<String, Double> scoreMembers, ZAddParams params) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.zadd(key, scoreMembers, params);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zadd", key);
        }
    }

    @Override
    public Set<String> zrange(String key, long start, long end) throws JedisException {
        long s = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Set<String> res = jedis.zrange(key, start, end);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(s, exception, returnNull, "zrange", key);
        }
    }

    @Override
    public Long zrem(String key, String... members) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.zrem(key, members);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zrem", key);
        }
    }

    @Override
    public Double zincrby(String key, double score, String member) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.zincrby(key, score, member);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zincrby", key);
        }
    }

    @Override
    public Double zincrby(String key, double score, String member, ZIncrByParams params) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.zincrby(key, score, member, params);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zincrby", key);
        }
    }

    @Override
    public Long zrank(String key, String member) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.zrank(key, member);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zrank", key);
        }
    }

    @Override
    public Long zrevrank(String key, String member) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.zrevrank(key, member);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zrevrank", key);
        }
    }

    @Override
    public Set<String> zrevrange(String key, long start, long end) throws JedisException {
        long s = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Set<String> res = jedis.zrevrange(key, start, end);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(s, exception, returnNull, "zrevrange", key);
        }
    }

    @Override
    public Set<Tuple> zrangeWithScores(String key, long start, long end) throws JedisException {
        long s = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Set<Tuple> res = jedis.zrangeWithScores(key, start, end);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(s, exception, returnNull, "zrangeWithScores", key);
        }
    }

    @Override
    public Set<Tuple> zrevrangeWithScores(String key, long start, long end) throws JedisException {
        long s = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Set<Tuple> res = jedis.zrevrangeWithScores(key, start, end);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(s, exception, returnNull, "zrevrangeWithScores", key);
        }
    }

    @Override
    public Long zcard(String key) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.zcard(key);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zcard", key);
        }
    }

    @Override
    public Double zscore(String key, String member) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.zscore(key, member);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zscore", key);
        }
    }

    @Override
    public String watch(String... keys) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.watch(keys);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "watch", keys);
        }
    }

    @Override
    public List<String> sort(String key) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.sort(key);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "sort", key);
        }
    }

    @Override
    public List<String> sort(String key, SortingParams sortingParameters) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.sort(key, sortingParameters);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "sort", key);
        }
    }

    @Override
    public List<String> blpop(int timeout, String... keys) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            List<String> res = jedis.blpop(timeout, keys);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "blpop", keys);
        }
    }

    @Override
    public List<String> blpop(String... args) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            List<String> res = jedis.blpop(args);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "blpop", args);
        }
    }

    @Override
    public List<String> brpop(String... args) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            List<String> res = jedis.brpop(args);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "brpop", args);
        }
    }

    @Override
    public Long sort(String key, SortingParams sortingParameters, String dstkey) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.sort(key, sortingParameters, dstkey);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "sort", key);
        }
    }

    @Override
    public Long sort(String key, String dstkey) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.sort(key, dstkey);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "sort", key);
        }
    }

    @Override
    public List<String> brpop(int timeout, String... keys) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            List<String> res = jedis.brpop(timeout, keys);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "brpop", keys);
        }
    }

    @Override
    public Long zcount(String key, double min, double max) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.zcount(key, min, max);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zcount", key);
        }
    }

    @Override
    public Long zcount(String key, String min, String max) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.zcount(key, min, max);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zcount", key);
        }
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Set<String> res = jedis.zrangeByScore(key, min, max);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zrangeByScore", key);
        }
    }

    @Override
    public Set<String> zrangeByScore(String key, String min, String max) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Set<String> res = jedis.zrangeByScore(key, min, max);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zrangeByScore", key);
        }
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Set<String> res = jedis.zrangeByScore(key, min, max, offset, count);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zrangeByScore", key);
        }
    }

    @Override
    public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Set<String> res = jedis.zrangeByScore(key, min, max, offset, count);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zrangeByScore", key);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Set<Tuple> res = jedis.zrangeByScoreWithScores(key, min, max);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zrangeByScoreWithScores", key);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Set<Tuple> res = jedis.zrangeByScoreWithScores(key, min, max);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zrangeByScoreWithScores", key);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Set<Tuple> res = jedis.zrangeByScoreWithScores(key, min, max, offset, count);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zrangeByScoreWithScores", key);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Set<Tuple> res = jedis.zrangeByScoreWithScores(key, min, max, offset, count);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zrangeByScoreWithScores", key);
        }
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Set<String> res = jedis.zrevrangeByScore(key, max, min);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zrevrangeByScore", key);
        }
    }

    @Override
    public Set<String> zrevrangeByScore(String key, String max, String min) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Set<String> res = jedis.zrevrangeByScore(key, max, min);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zrevrangeByScore", key);
        }
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Set<String> res = jedis.zrevrangeByScore(key, max, min, offset, count);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zrevrangeByScore", key);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Set<Tuple> res = jedis.zrevrangeByScoreWithScores(key, max, min);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zrevrangeByScoreWithScores", key);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Set<Tuple> res = jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zrevrangeByScoreWithScores", key);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Set<Tuple> res = jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zrevrangeByScoreWithScores", key);
        }
    }

    @Override
    public Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Set<String> res = jedis.zrevrangeByScore(key, max, min, offset, count);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zrevrangeByScore", key);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Set<Tuple> res = jedis.zrevrangeByScoreWithScores(key, max, min);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zrevrangeByScoreWithScores", key);
        }
    }

    @Override
    public Long zremrangeByRank(String key, long start, long end) throws JedisException {
        long s = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.zremrangeByRank(key, start, end);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(s, exception, returnNull, "zremrangeByRank", key);
        }
    }

    @Override
    public Long zremrangeByScore(String key, double start, double end) throws JedisException {
        long s = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.zremrangeByScore(key, start, end);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(s, exception, returnNull, "zremrangeByScore", key);
        }
    }

    @Override
    public Long zremrangeByScore(String key, String start, String end) throws JedisException {
        long s = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.zremrangeByScore(key, start, end);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(s, exception, returnNull, "zremrangeByScore", key);
        }
    }

    @Override
    public Long zunionstore(String dstkey, String... sets) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.zunionstore(dstkey, sets);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zunionstore", dstkey);
        }
    }

    @Override
    public Long zunionstore(String dstkey, ZParams params, String... sets) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.zunionstore(dstkey, params, sets);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zunionstore", dstkey);
        }
    }

    @Override
    public Long zinterstore(String dstkey, String... sets) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.zinterstore(dstkey, sets);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zinterstore", dstkey);
        }
    }

    @Override
    public Long zinterstore(String dstkey, ZParams params, String... sets) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.zinterstore(dstkey, params, sets);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zinterstore", dstkey);
        }
    }

    @Override
    public Long zlexcount(String key, String min, String max) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.zlexcount(key, min, max);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zlexcount", key);
        }
    }

    @Override
    public Set<String> zrangeByLex(String key, String min, String max) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Set<String> res = jedis.zrangeByLex(key, min, max);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zrangeByLex", key);
        }
    }

    @Override
    public Set<String> zrangeByLex(String key, String min, String max, int offset, int count) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Set<String> res = jedis.zrangeByLex(key, min, max, offset, count);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zrangeByLex", key);
        }
    }

    @Override
    public Set<String> zrevrangeByLex(String key, String max, String min) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Set<String> res = jedis.zrevrangeByLex(key, max, min);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zrevrangeByLex", key);
        }
    }

    @Override
    public Set<String> zrevrangeByLex(String key, String max, String min, int offset, int count) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Set<String> res = jedis.zrevrangeByLex(key, max, min, offset, count);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zrevrangeByLex", key);
        }
    }

    @Override
    public Long zremrangeByLex(String key, String min, String max) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.zremrangeByLex(key, min, max);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zremrangeByLex", key);
        }
    }

    @Override
    public Long strlen(String key) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.strlen(key);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "strlen", key);
        }
    }

    @Override
    public Long lpushx(String key, String... string) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.lpushx(key, string);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "lpushx", key);
        }
    }

    @Override
    public Long persist(String key) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.persist(key);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "persist", key);
        }
    }

    @Override
    public Long rpushx(String key, String... string) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.rpushx(key, string);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "rpushx", key);
        }
    }

    @Override
    public String echo(String string) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.echo(string);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "echo", string);
        }
    }

    @Override
    public Long linsert(String key, BinaryClient.LIST_POSITION where, String pivot, String value) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.linsert(key, where, pivot, value);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "linsert", key);
        }
    }

    @Override
    public String brpoplpush(String source, String destination, int timeout) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            String res = jedis.brpoplpush(source, destination, timeout);
            if (null == res)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "brpoplpush", source);
        }
    }

    @Override
    public Boolean setbit(String key, long offset, boolean value) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.setbit(key, offset, value);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "setbit", key);
        }
    }

    @Override
    public Boolean setbit(String key, long offset, String value) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.setbit(key, offset, value);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "setbit", key);
        }
    }

    @Override
    public Boolean getbit(String key, long offset) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.getbit(key, offset);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "getbit", key);
        }
    }

    @Override
    public Long setrange(String key, long offset, String value) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.setrange(key, offset, value);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "setrange", key);
        }
    }

    @Override
    public String getrange(String key, long startOffset, long endOffset) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            String res = jedis.getrange(key, startOffset, endOffset);
            if (null == res)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "getrange", key);
        }
    }

    @Override
    public Long bitpos(String key, boolean value) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.bitpos(key, value);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "bitpos", key);
        }
    }

    @Override
    public Long bitpos(String key, boolean value, BitPosParams params) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.bitpos(key, value, params);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "bitpos", key);
        }
    }

    @Override
    public Object eval(String script, int keyCount, String... params) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.eval(script, keyCount, params);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "eval", script);
        }
    }

    @Override
    public Object eval(String script, List<String> keys, List<String> args) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.eval(script, keys, args);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "eval", script);
        }
    }

    @Override
    public Object eval(String script) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.eval(script);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "eval", script);
        }
    }

    @Override
    public Object evalsha(String script) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.evalsha(script);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "evalsha", script);
        }
    }

    @Override
    public Object evalsha(String sha1, List<String> keys, List<String> args) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.evalsha(sha1, keys, args);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "evalsha", sha1);
        }
    }

    @Override
    public Object evalsha(String sha1, int keyCount, String... params) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.evalsha(sha1, keyCount, params);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "evalsha", sha1);
        }
    }

    @Override
    public Boolean scriptExists(String sha1) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.scriptExists(sha1);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "scriptExists", sha1);
        }
    }

    @Override
    public List<Boolean> scriptExists(String... sha1) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.scriptExists(sha1);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "scriptExists", sha1);
        }
    }

    @Override
    public String scriptLoad(String script) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.scriptLoad(script);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "scriptLoad", script);
        }
    }

    @Override
    public Long bitcount(String key) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.bitcount(key);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "bitcount", key);
        }
    }

    @Override
    public Long bitcount(String key, long start, long end) throws JedisException {
        long s = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.bitcount(key, start, end);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(s, exception, returnNull, "bitcount", key);
        }
    }

    @Override
    public Long bitop(BitOP op, String destKey, String... srcKeys) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.bitop(op, destKey, srcKeys);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "bitop", destKey);
        }
    }

    @Override
    public Long pexpire(String key, long milliseconds) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.pexpire(key, milliseconds);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "pexpire", key);
        }
    }

    @Override
    public Long pexpireAt(String key, long millisecondsTimestamp) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.pexpireAt(key, millisecondsTimestamp);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "pexpireAt", key);
        }
    }

    @Override
    public Long pttl(String key) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.pttl(key);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "pttl", key);
        }
    }

    @Override
    public String psetex(String key, long milliseconds, String value) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.psetex(key, milliseconds, value);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "psetex", key);
        }
    }

    @Override
    public String set(String key, String value, String nxxx) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.set(key, value, nxxx);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "set", key);
        }
    }

    @Override
    public String set(String key, String value, String nxxx, String expx, int time) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.set(key, value, nxxx, expx, time);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "set", key);
        }
    }

    @Override
    public ScanResult<String> scan(String cursor) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.scan(cursor);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "scan", cursor);
        }
    }

    @Override
    public ScanResult<String> scan(String cursor, ScanParams params) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.scan(cursor, params);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "scan", cursor);
        }
    }

    @Override
    public ScanResult<Map.Entry<String, String>> hscan(String key, String cursor) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.hscan(key, cursor);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "hscan", key);
        }
    }

    @Override
    public ScanResult<Map.Entry<String, String>> hscan(String key, String cursor, ScanParams params) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.hscan(key, cursor, params);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "hscan", key);
        }
    }

    @Override
    public ScanResult<String> sscan(String key, String cursor) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.sscan(key, cursor);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "sscan", key);
        }
    }

    @Override
    public ScanResult<String> sscan(String key, String cursor, ScanParams params) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.sscan(key, cursor, params);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "sscan", key);
        }
    }

    @Override
    public ScanResult<Tuple> zscan(String key, String cursor) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.zscan(key, cursor);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zscan", key);
        }
    }

    @Override
    public ScanResult<Tuple> zscan(String key, String cursor, ScanParams params) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.zscan(key, cursor, params);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "zscan", key);
        }
    }

    @Override
    public Long pfadd(String key, String... elements) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.pfadd(key, elements);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "pfadd", key);
        }
    }

    @Override
    public String pfmerge(String destkey, String... sourcekeys) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.pfmerge(destkey, sourcekeys);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "pfmerge", destkey);
        }
    }

    @Override
    public List<String> blpop(int timeout, String key) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            List<String> res = jedis.blpop(timeout, key);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "blpop", key);
        }
    }

    @Override
    public List<String> brpop(int timeout, String key) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            List<String> res = jedis.brpop(timeout, key);
            if (null == res || res.size() == 0)
                returnNull = true;
            return res;
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "brpop", key);
        }
    }

    @Override
    public Long geoadd(String key, double longitude, double latitude, String member) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.geoadd(key, longitude, latitude, member);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "geoadd", key);
        }
    }

    @Override
    public Long geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.geoadd(key, memberCoordinateMap);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "geoadd", key);
        }
    }

    @Override
    public Double geodist(String key, String member1, String member2) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.geodist(key, member1, member2);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "geodist", key);
        }
    }

    @Override
    public Double geodist(String key, String member1, String member2, GeoUnit unit) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.geodist(key, member1, member2, unit);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "geodist", key);
        }
    }

    @Override
    public List<String> geohash(String key, String... members) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.geohash(key, members);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "geohash", key);
        }
    }

    @Override
    public List<GeoCoordinate> geopos(String key, String... members) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.geopos(key, members);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "geopos", key);
        }
    }

    @Override
    public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.georadius(key, longitude, latitude, radius, unit);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "georadius", key);
        }
    }

    @Override
    public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.georadius(key, longitude, latitude, radius, unit, param);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "georadius", key);
        }
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.georadiusByMember(key, member, radius, unit);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "georadiusByMember", key);
        }
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit, GeoRadiusParam param) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.georadiusByMember(key, member, radius, unit, param);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "georadiusByMember", key);
        }
    }

    @Override
    public List<Long> bitfield(String key, String... arguments) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            return jedis.bitfield(key, arguments);
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "bitfield", key);
        }
    }

    @Override
    public List<Object> hmsetAfterDel(String key, int seconds, Map<String, String> hash) throws JedisException {
        long start = System.currentTimeMillis();
        boolean exception = false;
        boolean returnNull = false;
        Jedis jedis = getJedis();
        try {
            Transaction tx = jedis.multi();
            tx.del(new String[]{key});
            tx.hmset(key, hash);
            if (seconds > 0)
                tx.expire(key, seconds);

            return tx.exec();
        } catch (JedisException e) {
            exception = true;
            throw e;
        } finally {
            jedis.close();
            record(start, exception, returnNull, "hmsetAfterDel", key);
        }
    }

    @Override
    public synchronized Jedis getJedis() throws JedisException {
        return this.jedisSentinelPool.getResource();
    }

    @Override
    public synchronized BPipeline pipeline() throws JedisException {
        Jedis jedis = getJedis();
        return new BPipeline(jedis.pipelined(), jedis);
    }

    private void record(long start, boolean exception, boolean returnNull, String cmd, String... key) {
        try {
            if (null == key || key.length == 0)
                return;

            ActionRecord.record(this.appId, this.clusterId, (System.currentTimeMillis() - start), start, exception, returnNull, cmd, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void record(long start, boolean exception, boolean returnNull, String cmd, byte[]... key) {
        try {
            if (null == key || key.length == 0)
                return;

            String[] nKeys = new String[key.length];
            for (int i = 0; i < key.length; i++)
                nKeys[i] = new String(key[i]);

            ActionRecord.record(this.appId, this.clusterId, (System.currentTimeMillis() - start), start, exception, returnNull, cmd, nKeys);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Set<String> getHost(String sentinelHost) {
        Set<String> hosts = new HashSet<String>();
        Collections.addAll(hosts, sentinelHost.split(";"));
        return hosts;
    }

    private JedisSentinelPool jedisSentinelPool;

}
