package com.xcdh.target.redis.model;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisException;

import java.util.List;

/**
 * Created by MikeLC
 * Create Date: 2019/4/19 10:42
 * Description: ${DESCRIPTION}
 */
public class BPipeline {

    private Pipeline pipeline;
    private Jedis jedis;

    public BPipeline(Pipeline pipeline, Jedis jedis) {
        this.pipeline = pipeline;
        this.jedis = jedis;
    }

    public Pipeline pipeline(){
        return pipeline;
    }

    public List<Object> submitAndReturn() throws JedisException {
        if (null == this.pipeline || null == this.jedis)
            return null;

        try {
            return this.pipeline.syncAndReturnAll();
        } catch (JedisException e) {
            throw e;
        } finally {
            this.jedis.close();
        }
    }
}
