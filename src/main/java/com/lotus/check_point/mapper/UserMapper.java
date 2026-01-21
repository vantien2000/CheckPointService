package com.lotus.check_point.mapper;

import org.springframework.beans.BeanUtils;

import com.lotus.check_point.dto.UserDto;
import com.lotus.check_point.entity.UserEntity;

public class UserMapper {
    public static UserDto toMapperDto(UserEntity userEntity) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userEntity, userDto);
        return userDto;
    }
}
