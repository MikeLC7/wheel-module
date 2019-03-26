package com.xcdh.target.lock.blocked;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;

import java.util.concurrent.TimeUnit;

public class RedisThreadSync {  
  
	/**锁默认等待时间*/
    private static final int DEFAULT_SINGLE_EXPIRE_TIME = 60;  
    
    /*
     * Redis 
     */
    private static Logger logger = LoggerFactory.getLogger(RedisThreadSync.class);

    private static JedisPool jedisPool;

    public static JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        RedisThreadSync.jedisPool = jedisPool;
    }

    /**
     * 获取锁  如果锁可用   立即返回true，  否则返回false 
     *
     * @param  
     * @return 
     */  
    public boolean tryLock(String key) {  
        return tryLock(key, 0L, null);  
    }  
  
    /** 
     * 锁在给定的等待时间内空闲，则获取锁成功 返回true， 否则返回false 
     * @author http://www.jinbank.com.cn 
     * @param  
     * @param timeout 
     * @param unit 
     * @return 
     */  
    public boolean tryLock(String key, long timeout, TimeUnit unit) {  
        Jedis jedis = null;  
        try {  
            jedis = getResource();  
            long nano = System.nanoTime();  
            do {  
                logger.debug("try lock key: " + key);  
                Long i = jedis.setnx(key, key);  
                if (i == 1) {   
                    jedis.expire(key, DEFAULT_SINGLE_EXPIRE_TIME);  
                    logger.debug("get lock, key: " + key + " , expire in " + DEFAULT_SINGLE_EXPIRE_TIME + " seconds.");  
                    return Boolean.TRUE;  
                } else { // 存在锁  
                        String desc = jedis.get(key);  
                        logger.debug("key: " + key + " locked by another business：" + desc);  
                }  
                if (timeout == 0) {  
                    break;  
                }  
                Thread.sleep(300);  
            } while ((System.nanoTime() - nano) < unit.toNanos(timeout));  
            return Boolean.FALSE;  
        } catch (JedisConnectionException je) {  
        	logger.error(je.getMessage(), je);  
            returnBrokenResource(jedis);  
        } catch (Exception e) {  
        	logger.error(e.getMessage(), e);  
        } finally {  
            returnResource(jedis);  
        }  
        return Boolean.FALSE;  
    }  
  
    /** 
     * 如果锁空闲立即返回   获取失败 一直等待 
     * @author http://www.jinbank.com.cn 
     * @param
     */  
    public  boolean lock(String key) throws Exception {  
    	//增加计算redis线程锁耗时
    	String stamp = String.valueOf(System.currentTimeMillis());
    	return lock(key, stamp);
    }  
    
    /** 
     * 如果锁空闲立即返回   获取失败 一直等待 
     */
    public  boolean lock(String key, String value) throws Exception {  
    	//增加计算redis线程锁耗时
    	long startTime = System.currentTimeMillis();
    	long endTime = 0;
        Jedis jedis = null;  
        try {  
            jedis = getResource();  
            do {  
                logger.info("lock key: " + key);  
                Long i = jedis.setnx(key, value);  
                if (i == 1) {   
                    jedis.expire(key, DEFAULT_SINGLE_EXPIRE_TIME);  
                    logger.info("get lock, key: " + key + " , expire in " + DEFAULT_SINGLE_EXPIRE_TIME + " seconds.");  
                    return true;  
                } else {  
                        String desc = jedis.get(key);  
                        logger.info(" key: " + key + " locked by another business：" + desc+",application lock business:"+value);  
                }
                endTime = System.currentTimeMillis() - startTime;
                if((endTime - startTime)/1000 > DEFAULT_SINGLE_EXPIRE_TIME){
                	 throw new Exception("获取redis锁失败");
                }
                Thread.sleep(300);   
            } while (true);  
        } catch (JedisConnectionException je) {  
            logger.error(je.getMessage(), je);  
            returnBrokenResource(jedis);  
            throw new Exception("获取redis锁失败");
        } catch (Exception e) {  
            logger.error(e.getMessage(), e);
            throw new Exception("获取redis锁失败");
        } finally {  
            returnResource(jedis);  
            logger.info("获取redis锁耗时【"+(System.currentTimeMillis() - startTime)+"】毫秒");
        }  
    } 
    /** 
     * 批量释放锁 
     */
    public void unLock(String key) {  
        Jedis jedis = null;  
        try {  
            jedis = getResource();  
            jedis.del(key);  
            logger.info("release lock, keys :" + key);  
        } catch (JedisConnectionException je) {  
        	logger.error(je.getMessage(), je);  
            returnBrokenResource(jedis);  
        } catch (Exception e) {  
        	logger.error(e.getMessage(), e);  
        } finally {  
            returnResource(jedis);  
        }  
    }  
    /**
     * 
     * Description: 
     *
     * @author: MikeLC
     *
     * @date: 2017-4-18 下午7:29:42
     */
    private Jedis getResource() {  
        //return RedisFactory.jedisPool.getResource();
    	Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
//			logger.debug("getResource.", jedis);
		} catch (JedisException e) {
			logger.warn("getResource.", e);
			returnBrokenResource(jedis);
			throw e;
		}
		return jedis;
    }  
      
    /** 
     * 销毁连接 
     * @author http://www.jinbank.com.cn 
     * @param jedis 
     */  
    private void returnBrokenResource(Jedis jedis) {  
        if (jedis == null) {  
            return;  
        }  
        try {  
            //容错  
        	//RedisFactory. jedisPool.returnBrokenResource(jedis);  
        } catch (Exception e) {  
            logger.error(e.getMessage(), e);  
        }  
    }  
      
    /** 
     * 释放Redis资源
     * @author http://www.jinbank.com.cn 
     * @param jedis 
     */  
    private void returnResource(Jedis jedis) {  
        if (jedis == null) {  
            return;  
        }  
        try {  
        	//RedisFactory.jedisPool.returnResource(jedis);  
        } catch (Exception e) {  
            logger.error(e.getMessage(), e);  
        }  
    }
    
    
    /**
     * 校验当前值是否被占用
     * @param key
     * @param
     * @return
     * @throws Exception
     */
    public   boolean validateRedisLock(String key){  
        Jedis jedis = null;  
        try {  
            jedis = getResource();  
            Long i = jedis.setnx(key, key);  
            if (i == 1) {   
                jedis.expire(key, DEFAULT_SINGLE_EXPIRE_TIME);  
                return true;  
            } else {  
                   return false;
            }

        } catch (JedisConnectionException je) {  
            returnBrokenResource(jedis);  
        } catch (Exception e) {  
        	
        } finally {  
            returnResource(jedis);  
        }  
        return false;
    } 
    
    
}