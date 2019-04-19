package com.xcdh.target.util;


import com.xcdh.target.redis.RedisUtil;
import com.xcdh.target.util.idgen.IdgenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 骆俊武
 */
@Component
public class IdGenerator implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(IdGenerator.class);

    private static IdGenerator self;

    @Value("${redis.clusterId}")
    private int clusterId = 30;

    @Value("${redis.sentinel}")
    private String sentinelHost;

    @Value("${redis.masterName}")
    private String masterName;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 生成以业务码为前缀的19位long型ID，需要定义系统内唯一业务码
     */
    public enum BusinessId{
        /**
         * 测试业务
         */
        TEST_BUSINESS(10, "测试业务");

        private int id;
        private String name;

        BusinessId(int id, String name){
            this.id = id;
            this.name = name;
        }
    }


    /**
     * Spring加载类之后，会执行该方法初始化IdgenUtil，
     * 并将加载之后的 IdGenerater 对象赋值给self
     * @param event
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            self = this;
            IdgenUtil.init(Constant.SYSTEM_NAME, clusterId, sentinelHost, masterName);
        } catch (Exception e) {
            LOGGER.error("初始化ID生成器异常", e);
        }
    }

    /**
     * 生成ID的方法      <br/>
     * 时钟回调会IdgenUtil会抛出异常，降级采用时间戳+redis生成序号的方式生成id
     * @param businessId
     * @return
     */
    public static long generate(BusinessId businessId){
        try {
            return IdgenUtil.generateCombine(businessId.id);
        } catch (Exception e) {
            LOGGER.warn("ID生成器异常", e);
        }
        // 降级使用基于redis的生成器
        return self.redisUtil.generateId(businessId.id);
    }

}
