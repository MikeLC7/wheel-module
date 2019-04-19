package com.xcdh.target.redis;

public class RedisKeyConst {
    /**
     * 通用类前缀
      */
    private static final String PREFIX_LOCK = "ark:lock:";
    private static final String PREFIX_CACHE = "ark:cache:";
    /**
     * 基础设置
     */
    public static final Integer EXPIRE_TIME_ONE_MINUTE = 60;
    public static final Integer EXPIRE_TIME_ONE_HOUR = 60 * 60;
    public static final Integer EXPIRE_TIME_ONE_DAY = EXPIRE_TIME_ONE_HOUR * 24;
    public static final Integer EXPIRE_TIME_ONE_WEEK = EXPIRE_TIME_ONE_DAY * 7;
    public static final Integer EXPIRE_TIME_ONE_MONTH = EXPIRE_TIME_ONE_WEEK * 4;
    public static final Integer RESOURCE_EXPIRE_TIME = 10 * EXPIRE_TIME_ONE_MINUTE;
    /**
     * 业务字段
     */
    public static final String USER_INFO_STORAGE = "user:user:";
    public static final String USER_RESOURCE_STORAGE = "user:resource:";
    public static final String USER_NAME = "username";
    public static final String USER_NICK_NAME = "usernickname";
    public static final String USER_DESC = "userDesc";
    public static final String USER_MOBILE = "usermobile";
    public static final String USER_TOCKEN = "tocken";
    public static final Integer USER_RESOURCE_EXP = 1800;
    public static final Integer USER_LOGIN_TIME_EXP = 7200;

    /**
     * 用户权限内容Key
     * @param uid
     * @return
     */
    public static RedisKey RESOURCE_UID_KEY(Long uid) {
        return new RedisKey(PREFIX_CACHE + USER_RESOURCE_STORAGE + "us_" + uid, RESOURCE_EXPIRE_TIME, uid);
    }

    /**
     * 用户信息内容Key
     * @param uid
     * @return
     */
    public static RedisKey LOGININFO_UID_KEY(Long uid) {
        return new RedisKey(PREFIX_CACHE + USER_INFO_STORAGE + "us_" + uid, RESOURCE_EXPIRE_TIME, uid);
    }


}

