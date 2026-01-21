package com.lotus.check_point.distributed.impl;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import com.lotus.check_point.distributed.RedissonDistributedLocker;
import com.lotus.check_point.distributed.RedissonDistributedService;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RedissonDistributedServiceImpl implements RedissonDistributedService {
    @Resource
    private RedissonClient redissonClient;
    @Override
    public RedissonDistributedLocker getDistributedLocker(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);

        return new RedissonDistributedLocker() {

            @Override
            public boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
                boolean isLockSuccess = lock.tryLock(waitTime, leaseTime, unit); // đã lock
                log.info("{} get lock result {}", lockKey, isLockSuccess);
                return isLockSuccess;
            }

            @Override
            public void unlock() {
                if (isLocked() & isHeldByCurrentThread()) // nếu đã lock thì mới unlock
                    lock.unlock();
            }

            @Override
            public void lock(long leaseTime, TimeUnit unit) {
                lock.lock(leaseTime, unit);
            }

            @Override
            public boolean isLocked() {
                return lock.isLocked();
            }

            @Override
            public boolean isHeldByThread(long threadId) {
                return lock.isHeldByThread(threadId); // là giữ luồng với thread id
            }

            @Override
            public boolean isHeldByCurrentThread() {
                return lock.isHeldByCurrentThread(); // là giữ luồng hiện tại
            }
            
        };
    }
    
}
