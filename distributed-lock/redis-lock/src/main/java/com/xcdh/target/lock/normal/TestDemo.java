package com.xcdh.target.lock.normal;

import com.xcdh.target.lock.normal.utilDemoA.RedisDistributedLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * Project: Wheel
 *
 * Description:
 *
 * @author: MikeLC
 *
 * @date: 2019/3/26 19:31
 **/
public class TestDemo {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisTemplate);

    public void lockMethod(){
        String demoKeyStr = "123123";
        redisDistributedLock.lock(demoKeyStr);
        redisDistributedLock.releaseLock(demoKeyStr);
    }

}
