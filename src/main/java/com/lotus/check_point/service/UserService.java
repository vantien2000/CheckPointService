package com.lotus.check_point.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lotus.check_point.dto.request.UserCreateRequest;
import com.lotus.check_point.entity.UserEntity;

public interface UserService {
    public UserEntity createUser(UserCreateRequest userCreateRequest);

    public Page<UserEntity> getAllUsers(Pageable pageable);
}