package com.lotus.check_point.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lotus.check_point.dto.UserDto;
import com.lotus.check_point.dto.request.UserCreateRequest;
import com.lotus.check_point.dto.response.base.ApiResponse;
import com.lotus.check_point.entity.UserEntity;
import com.lotus.check_point.mapper.UserMapper;
import com.lotus.check_point.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("v1/api")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/createUser")
    public ResponseEntity createUser(@RequestBody UserCreateRequest request) {
        try {
            UserEntity userEntity = userService.createUser(request);
            UserDto userDto = UserMapper.toMapperDto(userEntity);
            return new ResponseEntity<>(ApiResponse.success(userDto), HttpStatus.OK);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
    
    @GetMapping("/users")
    public Page<UserDto> getAllUser(
        @RequestParam int page,
        @RequestParam int size,
        @RequestParam(defaultValue = "id") String sort,
        @RequestParam(defaultValue = "desc") String direction

    ) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sortBy = Sort.by(sortDirection, sort);
        Pageable pageable = PageRequest.of(page, size, sortBy);
        return userService.getAllUsers(pageable).map(entity -> UserMapper.toMapperDto(entity));
    }
}
