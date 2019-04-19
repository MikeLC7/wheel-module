package com.xcdh.target.constant;

/**
 * Project: ARK
 *
 * Description:
 *
 * @author: MikeLC
 *
 * @date: 2019/4/10 11:16
 **/
public interface UserConstant {

    // 用户登录信息Cookie存储路径
    public static final String SSO_TOKEN_STORAGE = "SSOtoken";
    // 验证码错误次数限制
    public static final Integer VEROFY_CODE_LIMIT_NUM = 3;
    // 用户密码加密统一加盐字符串
    public static final String ENCRYPT_PW_SALT = "arkEncryptPwSalt";
    // 用户初始密码
    public static final String INIT_DEFAULT_PW = "123456";
    // 用户cookieToken分隔符
    public static final String USER_COOKIE_TOKEN_SPLIT_STR = "#";
    // 用户cookieToken分隔符加盐字符串
    public static final String USER_COOKIE_TOKEN_SALT = "Xxasf";

    // 需要修改密码标识 0 不需要修改 1 需要修改
    public static final int IS_SHOULD_MODIFY_PASSWORD = 1;
    public static final int IS_NOT_SHOULD_MODIFY_PASSWORD = 0;


    public static final String APP_ACCOUNT_TOKEN_STORAGE_COOKIE = "byAppToken";
    public static final String APP_ACCOUNT_INFO_STORAGE_COOKIE = "byAppUserId";
    public static final String PLATFORM_ACCOUNT_TYPE_CODE = "000000";


    public static final int ENABLE = 0;
    public static final int DISABLE = 1;
    public static final Integer USER_NAME_RANGE = 100;
    public static final String NAME = "@idstaff.com";
    public static final int DEFAULT_ACCOUNT_LIMIT = 10;

    // 登录校验状态
    public static final int VERIFYCODE_ERROR = 0;
    public static final int LOGIN_INFO_ERROR = 2;
    public static final int VERIFYCODE_EMPTY = 3;
    public static final int MAX_LOGIN_ERROR_NUM = 3;

    // 是否平台管理员 0 否 1是
    public static final int IS_NOT_PLATFORM_ADMIN = 0;
    public static final int IS_PLATFORM_ADMIN = 1;

    // 账号类型 0 平台 1 商家
    public static final String PLATFORM_USER_TYPE = "平台";
    public static final String SUPPLIER_USER_TYPE = "商家";

    // 账号类型 0 平台 1 商家
    public static final Integer PLATFORM_USER_TYPE_NUM = 0;
    public static final Integer SUPPLIER_USER_TYPE_NUM = 1;

    // 内置角色是否勾选 1：选中 0：未选中
    public static final int IS_CHECK = 1;
    public static final int IS_NOT_CHECK = 0;

    // 是否需要用户类型合并 1 是 0 否
    public static final int IS_NEED = 1;
    public static final int IS_NOT_NEED = 0;

    public static final Integer USER_NAME_MAX_RANGE = 15;

    public static final Integer USER_NAME_MIN_RANGE = 4;

    public static final Integer USER_FULL_NAME_MAX_RANGE = 10;

    public static final Integer USER_FULL_NAME_MIN_RANGE = 2;



    // 投诉单提醒
    public static final Integer COMPLIANT_TYPE = 23;
    // 手机验证码类型
    public static final Integer MOBILE_TYPE = 14;

    // 手机验证码发送平台
    public static final Integer MOBILE_PLATFORM = 4;

    // 手机验证码UUID
    public static final String MOBILE_UUID = "CS_000000000000000000000_";

    // 6月期限
    public static final Integer USER_PASSWORD_TIME= -6;

    // 用户异地登录标识
    public static final String USER_LOGIN_IP_TYPE = "1";
    // 帐号类型
    public static final String LIVE800_ACCOUNT_TYPE = "1";




}
