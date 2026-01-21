package com.lotus.check_point.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.lotus.check_point.dto.request.UserCreateRequest;
import com.lotus.check_point.entity.UserEntity;
import com.lotus.check_point.repository.UserRepository;
import com.lotus.check_point.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserEntity createUser(UserCreateRequest userCreateRequest) {
        if (userCreateRequest == null) throw new RuntimeException("user not found!");
        if (userCreateRequest.username.isEmpty() || userCreateRequest.password.isEmpty()) throw new RuntimeException("Username or password is required!");
        //get user exists in db
        UserEntity userExists = userRepository.findByUsername(userCreateRequest.username);
        if (userExists != null) throw new RuntimeException("User info is existed in system!");

        UserEntity userEntity = new UserEntity();
        userEntity.setFullName(userCreateRequest.fullName);
        // check trung user name
        userEntity.setUsername(userCreateRequest.username);
        // bcrypt password
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userEntity.setPassword(encoder.encode(userCreateRequest.password));
        userEntity.setPhoneNumber(userCreateRequest.phoneNumber);
        userEntity.setAddress(userCreateRequest.address);
        userEntity.setCreatedAt(LocalDateTime.now());
        userRepository.save(userEntity);
        return userEntity;
    }
    @Override
    public Page<UserEntity> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    
}
