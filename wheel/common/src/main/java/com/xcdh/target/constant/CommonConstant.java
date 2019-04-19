package com.xcdh.target.constant;

/**
 * Project: ARK
 *
 * Description:
 *
 * @author: MikeLC
 *
 * @date: 2019/4/9 20:51
 **/
public class CommonConstant {

    public static String cookieDomain = "ark.biyao.com";

    public static String SYSTEM_NAME = "ARK";

    public static String COMMON_SPLIT_STR = ",";

    public static String DESC_SPLIT_STR = "-";

    public static Integer DEFAULT_RESOURCE_LEVEL_NUM = 1;

    public static Long ROOT_RESOURCE_PARENT_ID = -1L;


    public static Integer CAN_FLAG_YES = 1;

    public static Integer CAN_FLAG_NO = 0;


    /**
     * 缓存锁过期时间10s
     */
    public void setCookieDomain(String cookieDomain) {
        CommonConstant.cookieDomain = cookieDomain;
    }

}
