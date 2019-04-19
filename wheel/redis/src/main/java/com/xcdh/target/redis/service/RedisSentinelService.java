package com.xcdh.target.redis.service;

import com.xcdh.target.redis.model.BPipeline;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by MikeLC
 * Create Date: 2019/4/15 17:32
 * Description: ${DESCRIPTION}
 */
public interface RedisSentinelService {


    String set(String var1, String var2) throws JedisException;

    String set(byte[] var1, byte[] var2) throws JedisException;

    String get(String var1) throws JedisException;

    byte[] get(byte[] var1) throws JedisException;

    String getSet(String var1, String var2) throws JedisException;

    Long setnx(String var1, String var2) throws JedisException;

    Long setnx(byte[] var1, byte[] var2) throws JedisException;

    Long del(String var1) throws JedisException;

    Long del(byte[] var1) throws JedisException;

    String set(String key, String value, String nxxx, String expx, long time) throws JedisException;

    String set(byte[] key, byte[] value, byte[] nxxx, byte[] expx, long time) throws JedisException;

    Long exists(String... keys) throws JedisException;

    Boolean exists(String key) throws JedisException;

    Long del(String... keys) throws JedisException;

    String type(String key) throws JedisException;

    Set<String> keys(String pattern) throws JedisException;

    String randomKey() throws JedisException;

    String rename(String oldkey, String newkey) throws JedisException;

    Long renamenx(String oldkey, String newkey) throws JedisException;

    Long expire(String key, int seconds) throws JedisException;

    Long expire(byte[] key, int seconds) throws JedisException;

    Long expireAt(String key, long unixTime) throws JedisException;

    Long expireAt(byte[] key, long unixTime) throws JedisException;

    Long ttl(String key) throws JedisException;

    Long ttl(byte[] key) throws JedisException;

    List<String> mget(String... keys) throws JedisException;

    List<byte[]> mget(byte[]... keys) throws JedisException;

    String setex(String key, int seconds, String value) throws JedisException;

    String setex(byte[] key, int seconds, byte[] value) throws JedisException;

    String mset(String... keysvalues) throws JedisException;

    String mset(byte[]... keysvalues) throws JedisException;

    Long msetnx(String... keysvalues) throws JedisException;

    Long decrBy(String key, long integer) throws JedisException;

    Long decr(String key) throws JedisException;

    Long incrBy(String key, long integer) throws JedisException;

    Double incrByFloat(String key, double value) throws JedisException;

    Long incr(String key) throws JedisException;

    Long append(String key, String value) throws JedisException;

    String substr(String key, int start, int end) throws JedisException;

    Long hset(String key, String field, String value) throws JedisException;

    Long hset(byte[] key, byte[] field, byte[] value) throws JedisException;

    String hget(String key, String field) throws JedisException;

    byte[] hget(byte[] key, byte[] field) throws JedisException;

    Long hsetnx(String key, String field, String value) throws JedisException;

    Long hsetnx(byte[] key, byte[] field, byte[] value) throws JedisException;

    String hmset(String key, Map<String, String> hash) throws JedisException;

    String hmset(byte[] key, Map<byte[], byte[]> hash) throws JedisException;

    List<String> hmget(String key, String... fields) throws JedisException;

    List<byte[]> hmget(byte[] key, byte[]... fields) throws JedisException;

    Long hincrBy(String key, String field, long value) throws JedisException;

    Double hincrByFloat(String key, String field, double value) throws JedisException;

    Boolean hexists(String key, String field) throws JedisException;

    Long hdel(String key, String... fields) throws JedisException;

    Long hdel(byte[] key, byte[]... fields) throws JedisException;

    Long hlen(String key) throws JedisException;

    Set<String> hkeys(String key) throws JedisException;

    List<String> hvals(String key) throws JedisException;

    Map<String, String> hgetAll(String key) throws JedisException;

    Map<byte[], byte[]> hgetAll(byte[] key) throws JedisException;

    Long rpush(String key, String... strings) throws JedisException;

    Long rpush(byte[] key, byte[]... strings) throws JedisException;

    Long lpush(String key, String... strings) throws JedisException;

    Long lpush(byte[] key, byte[]... strings) throws JedisException;

    Long llen(String key) throws JedisException;

    List<String> lrange(String key, long start, long end) throws JedisException;

    String ltrim(String key, long start, long end) throws JedisException;

    String lindex(String key, long index) throws JedisException;

    String lset(String key, long index, String value) throws JedisException;

    Long lrem(String key, long count, String value) throws JedisException;

    String lpop(String key) throws JedisException;

    byte[] lpop(byte[] key) throws JedisException;

    String rpop(String key) throws JedisException;

    byte[] rpop(byte[] key) throws JedisException;

    String rpoplpush(String srckey, String dstkey) throws JedisException;

    Long sadd(String key, String... members) throws JedisException;

    Set<String> smembers(String key) throws JedisException;

    Long srem(String key, String... members) throws JedisException;

    String spop(String key) throws JedisException;

    Set<String> spop(String key, long count) throws JedisException;

    Long smove(String srckey, String dstkey, String member) throws JedisException;

    Long scard(String key) throws JedisException;

    Boolean sismember(String key, String member) throws JedisException;

    Set<String> sinter(String... keys) throws JedisException;

    Long sinterstore(String dstkey, String... keys) throws JedisException;

    Set<String> sunion(String... keys) throws JedisException;

    Long sunionstore(String dstkey, String... keys) throws JedisException;

    Set<String> sdiff(String... keys) throws JedisException;

    Long sdiffstore(String dstkey, String... keys) throws JedisException;

    String srandmember(String key) throws JedisException;

    List<String> srandmember(String key, int count) throws JedisException;

    Long zadd(String key, double score, String member) throws JedisException;

    Long zadd(String key, double score, String member, ZAddParams params) throws JedisException;

    Long zadd(String key, Map<String, Double> scoreMembers) throws JedisException;

    Long zadd(String key, Map<String, Double> scoreMembers, ZAddParams params) throws JedisException;

    Set<String> zrange(String key, long start, long end) throws JedisException;

    Long zrem(String key, String... members) throws JedisException;

    Double zincrby(String key, double score, String member) throws JedisException;

    Double zincrby(String key, double score, String member, ZIncrByParams params) throws JedisException;

    Long zrank(String key, String member) throws JedisException;

    Long zrevrank(String key, String member) throws JedisException;

    Set<String> zrevrange(String key, long start, long end) throws JedisException;

    Set<Tuple> zrangeWithScores(String key, long start, long end) throws JedisException;

    Set<Tuple> zrevrangeWithScores(String key, long start, long end) throws JedisException;

    Long zcard(String key) throws JedisException;

    Double zscore(String key, String member) throws JedisException;

    String watch(String... keys) throws JedisException;

    List<String> sort(String key) throws JedisException;

    List<String> sort(String key, SortingParams sortingParameters) throws JedisException;

    List<String> blpop(int timeout, String... keys) throws JedisException;

    List<String> blpop(String... args) throws JedisException;

    List<String> brpop(String... args) throws JedisException;

    Long sort(String key, SortingParams sortingParameters, String dstkey) throws JedisException;

    Long sort(String key, String dstkey) throws JedisException;

    List<String> brpop(int timeout, String... keys) throws JedisException;

    Long zcount(String key, double min, double max) throws JedisException;

    Long zcount(String key, String min, String max) throws JedisException;

    Set<String> zrangeByScore(String key, double min, double max) throws JedisException;

    Set<String> zrangeByScore(String key, String min, String max) throws JedisException;

    Set<String> zrangeByScore(String key, double min, double max, int offset, int count) throws JedisException;

    Set<String> zrangeByScore(String key, String min, String max, int offset, int count) throws JedisException;

    Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) throws JedisException;

    Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) throws JedisException;

    Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) throws JedisException;

    Set<Tuple> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) throws JedisException;

    Set<String> zrevrangeByScore(String key, double max, double min) throws JedisException;

    Set<String> zrevrangeByScore(String key, String max, String min) throws JedisException;

    Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) throws JedisException;

    Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) throws JedisException;

    Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) throws JedisException;

    Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) throws JedisException;

    Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) throws JedisException;

    Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min) throws JedisException;

    Long zremrangeByRank(String key, long start, long end) throws JedisException;

    Long zremrangeByScore(String key, double start, double end) throws JedisException;

    Long zremrangeByScore(String key, String start, String end) throws JedisException;

    Long zunionstore(String dstkey, String... sets) throws JedisException;

    Long zunionstore(String dstkey, ZParams params, String... sets) throws JedisException;

    Long zinterstore(String dstkey, String... sets) throws JedisException;

    Long zinterstore(String dstkey, ZParams params, String... sets) throws JedisException;

    Long zlexcount(String key, String min, String max) throws JedisException;

    Set<String> zrangeByLex(String key, String min, String max) throws JedisException;

    Set<String> zrangeByLex(String key, String min, String max, int offset, int count) throws JedisException;

    Set<String> zrevrangeByLex(String key, String max, String min) throws JedisException;

    Set<String> zrevrangeByLex(String key, String max, String min, int offset, int count) throws JedisException;

    Long zremrangeByLex(String key, String min, String max) throws JedisException;

    Long strlen(String key) throws JedisException;

    Long lpushx(String key, String... string) throws JedisException;

    Long persist(String key) throws JedisException;

    Long rpushx(String key, String... string) throws JedisException;

    String echo(String string) throws JedisException;

    Long linsert(String key, BinaryClient.LIST_POSITION where, String pivot, String value) throws JedisException;

    String brpoplpush(String source, String destination, int timeout) throws JedisException;

    Boolean setbit(String key, long offset, boolean value) throws JedisException;

    Boolean setbit(String key, long offset, String value) throws JedisException;

    Boolean getbit(String key, long offset) throws JedisException;

    Long setrange(String key, long offset, String value) throws JedisException;

    String getrange(String key, long startOffset, long endOffset) throws JedisException;

    Long bitpos(String key, boolean value) throws JedisException;

    Long bitpos(String key, boolean value, BitPosParams params) throws JedisException;

    Object eval(String script, int keyCount, String... params) throws JedisException;

    Object eval(String script, List<String> keys, List<String> args) throws JedisException;

    Object eval(String script) throws JedisException;

    Object evalsha(String script) throws JedisException;

    Object evalsha(String sha1, List<String> keys, List<String> args) throws JedisException;

    Object evalsha(String sha1, int keyCount, String... params) throws JedisException;

    Boolean scriptExists(String sha1) throws JedisException;

    List<Boolean> scriptExists(String... sha1) throws JedisException;

    String scriptLoad(String script) throws JedisException;

    Long bitcount(String key) throws JedisException;

    Long bitcount(String key, long start, long end) throws JedisException;

    Long bitop(BitOP op, String destKey, String... srcKeys) throws JedisException;

    Long pexpire(String key, long milliseconds) throws JedisException;

    Long pexpireAt(String key, long millisecondsTimestamp) throws JedisException;

    Long pttl(String key) throws JedisException;

    String psetex(String key, long milliseconds, String value) throws JedisException;

    String set(String key, String value, String nxxx) throws JedisException;

    String set(String key, String value, String nxxx, String expx, int time) throws JedisException;

    ScanResult<String> scan(String cursor) throws JedisException;

    ScanResult<String> scan(String cursor, ScanParams params) throws JedisException;

    ScanResult<Map.Entry<String, String>> hscan(String key, String cursor) throws JedisException;

    ScanResult<Map.Entry<String, String>> hscan(String key, String cursor, ScanParams params) throws JedisException;

    ScanResult<String> sscan(String key, String cursor) throws JedisException;

    ScanResult<String> sscan(String key, String cursor, ScanParams params) throws JedisException;

    ScanResult<Tuple> zscan(String key, String cursor) throws JedisException;

    ScanResult<Tuple> zscan(String key, String cursor, ScanParams params) throws JedisException;

    Long pfadd(String key, String... elements) throws JedisException;

    String pfmerge(String destkey, String... sourcekeys) throws JedisException;

    List<String> blpop(int timeout, String key) throws JedisException;

    List<String> brpop(int timeout, String key) throws JedisException;

    Long geoadd(String key, double longitude, double latitude, String member) throws JedisException;

    Long geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap) throws JedisException;

    Double geodist(String key, String member1, String member2) throws JedisException;

    Double geodist(String key, String member1, String member2, GeoUnit unit) throws JedisException;

    List<String> geohash(String key, String... members) throws JedisException;

    List<GeoCoordinate> geopos(String key, String... members) throws JedisException;

    List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit) throws JedisException;

    List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) throws JedisException;

    List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit) throws JedisException;

    List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit, GeoRadiusParam param) throws JedisException;

    List<Long> bitfield(String key, String... arguments) throws JedisException;

    List<Object> hmsetAfterDel(String key, int seconds, Map<String, String> hash) throws JedisException;

    Jedis getJedis() throws JedisException;

    BPipeline pipeline() throws JedisException;

}
