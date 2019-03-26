package com.xcdh.target.lock.normal.constant;

/**
 * Project: Wheel
 *
 * Description: Redis主键示例：用来封装Redis相关操作需要的字段；
 *
 * @author: MikeLC
 *
 * @date: 2019/3/26 20:01
 **/
public class RedisKey {

    /**
     * key
     */
    private String key;

    private String field;

    /**
     * 组装key使用的id
     */
    private Long id;

    /**
     * 有效期
     */
    private int expireSeconds;

    /**
     * db锁key 防止并发操作
     */
    private String loadLockKey;

    /**
     * db锁有效期
     */
    private int loadLockExpireSeconds = 8;

    /**
     * 刷新锁key 防止空值穿透
     */
    private String refreshLockKey;

    /**
     * 刷新锁有效期 TODO 30秒太长 具体待定
     */
    private int refreshLockExpireSeconds = 10;


    public RedisKey(String key, int expireSeconds) {
        this.key = key;
        this.expireSeconds = expireSeconds;
        this.loadLockKey = key + "_L";
        this.refreshLockKey = key + "_R";
    }


    public RedisKey(String key, int expireSeconds , Long id) {
        this.id = id;
        this.key = key;
        this.expireSeconds = expireSeconds;
        this.loadLockKey = key + "_L";
        this.refreshLockKey = key + "_R";
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getExpireSeconds() {
        return expireSeconds;
    }

    public void setExpireSeconds(int expireSeconds) {
        this.expireSeconds = expireSeconds;
    }

    public String getLoadLockKey() {
        return loadLockKey;
    }

    public void setLoadLockKey(String loadLockKey) {
        this.loadLockKey = loadLockKey;
    }

    public int getLoadLockExpireSeconds() {
        return loadLockExpireSeconds;
    }

    public void setLoadLockExpireSeconds(int loadLockExpireSeconds) {
        this.loadLockExpireSeconds = loadLockExpireSeconds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getRefreshLockKey() {
        return refreshLockKey;
    }

    public void setRefreshLockKey(String refreshLockKey) {
        this.refreshLockKey = refreshLockKey;
    }

    public int getRefreshLockExpireSeconds() {
        return refreshLockExpireSeconds;
    }

    public void setRefreshLockExpireSeconds(int refreshLockExpireSeconds) {
        this.refreshLockExpireSeconds = refreshLockExpireSeconds;
    }
}
