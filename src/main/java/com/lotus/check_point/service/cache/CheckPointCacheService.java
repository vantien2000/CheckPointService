package com.lotus.check_point.service.cache;

import com.lotus.check_point.entity.CheckPointEntity;

public interface CheckPointCacheService {
    public CheckPointEntity checkPointDaily(Long userId, boolean checked);

    public void unCheckPointDaily();
}
