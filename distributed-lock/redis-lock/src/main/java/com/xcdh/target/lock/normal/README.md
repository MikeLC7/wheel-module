------
Lock主目录

------
- constant：
    - RedisKey：Redis主键实体封装类；
- abstractClass：
    - DistributedLock：
        - 统一约定规范；
        - 具体有Redis分布式锁全量方法定义&默认常量定义；
        
    - AbstractDistributedLock：
        - 统一方法复用，减少实现类的负担；
        - 复用基本方法重载构建常用方法；
        - 并且实现Lock的重试机制；
        - 具体实现类，只需实现一个加锁的基础方法&解锁方法即可； 

- utilDemoA：完整版示例
    - RedisLock：redisTemplate版本；
- utilDemoB：精简版示例
    - RedisTool：工具类；

 

------