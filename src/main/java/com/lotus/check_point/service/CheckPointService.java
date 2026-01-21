package com.lotus.check_point.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lotus.check_point.entity.CheckPointEntity;

public interface CheckPointService {
    public Page<CheckPointEntity> getAllCheckpoint(Long userId, Pageable pageable);

    public Page<CheckPointEntity> getAllCheked(Long userId, String status, Pageable pageable);
}
