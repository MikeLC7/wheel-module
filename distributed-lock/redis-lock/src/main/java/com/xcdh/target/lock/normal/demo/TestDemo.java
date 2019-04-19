package com.xcdh.target.lock.normal.demo;

import com.xcdh.target.lock.normal.impl.utilDemoA.RedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

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
    RedisLock redisDistributedLock = new RedisLock(redisTemplate);

    public void lockDemo(){
        String demoKeyStr = "123123";
        redisDistributedLock.lock(demoKeyStr);
        redisDistributedLock.releaseLock(demoKeyStr);
    }

    /**
     * 伪代码
     */
    public void pseudoCode(){
        /**
         *
         * DemoA
         *
         */
        try {
            //if !(LockUtil.lock(lockKeyStr)){
            //     return; // or throw SomeException();
            // }

            // the business code

        } catch (Exception e){

        } finally {
            /**
             * release the lock
             */
            // LockUtil.releaseLock(lockKeyStr);
        }
    }



}
