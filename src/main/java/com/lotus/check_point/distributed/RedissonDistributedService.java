package com.lotus.check_point.distributed;

public interface RedissonDistributedService {
    public RedissonDistributedLocker getDistributedLocker(String lockKey);
    
}