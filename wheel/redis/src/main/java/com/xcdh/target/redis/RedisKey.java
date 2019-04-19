package com.xcdh.target.redis;



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
     * 刷新key：协助支持缓存防穿透，存放“默认值”
     */
    private String refreshKey;

    /**
     * 刷新key超时时间：理论上应很短；
     */
    private int refreshExpireSeconds = 5;


    public RedisKey(String key, int expireSeconds) {
        this.key = key;
        this.expireSeconds = expireSeconds;
        this.loadLockKey = key + "_L";
        this.refreshKey = key + "_R";
    }


    public RedisKey(String key, int expireSeconds , Long id) {
        this.id = id;
        this.key = key;
        this.expireSeconds = expireSeconds;
        this.loadLockKey = key + "_L";
        this.refreshKey = key + "_R";
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

    public String getRefreshKey() {
        return refreshKey;
    }

    public void setRefreshKey(String refreshKey) {
        this.refreshKey = refreshKey;
    }

    public int getRefreshExpireSeconds() {
        return refreshExpireSeconds;
    }

    public void setRefreshExpireSeconds(int refreshExpireSeconds) {
        this.refreshExpireSeconds = refreshExpireSeconds;
    }
}
