package com.lotus.check_point.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lotus.check_point.entity.CheckPointEntity;
import com.lotus.check_point.repository.CheckPointRepository;
import com.lotus.check_point.service.CheckPointService;

@Service
public class CheckPointServiceImpl implements CheckPointService {

    @Autowired
    private CheckPointRepository checkPointRepository;

    @Override
    public Page<CheckPointEntity> getAllCheckpoint(Long userId, Pageable pageable) {
        return checkPointRepository.findByUsersId(userId, pageable);
    }

    @Override
    public Page<CheckPointEntity> getAllCheked(Long userId, String status, Pageable pageable) {
        return checkPointRepository.findByUsersIdAndStatus(userId, status, pageable);
    }
}
