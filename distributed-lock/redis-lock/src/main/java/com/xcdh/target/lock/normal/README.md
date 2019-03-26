------
Lock主目录

------
- constant：
    - RedisKey：Redis主键实体封装类；
- utilDemoA：完整版示例
    - DistributedLock：接口形式定义基本方法和默认常量；
    - AbstractDistributedLock：复用基本方法重载构建常用方法；
    - RedisDistributedLock：具体实现类；
    - TestDemo：测试类；
- utilDemoB：精简版示例
    - RedisTool：工具类；

 

------