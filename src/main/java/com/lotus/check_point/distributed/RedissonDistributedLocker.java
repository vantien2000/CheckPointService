package com.lotus.check_point.distributed;

import java.util.concurrent.TimeUnit;

public interface RedissonDistributedLocker {
    boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException;

    void unlock(); 

    void lock(long leaseTime, TimeUnit unit);

    boolean isLocked();

    boolean isHeldByThread(long threadId);

    boolean isHeldByCurrentThread();
}
