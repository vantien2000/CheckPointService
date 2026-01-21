package com.lotus.check_point.mapper;

import org.springframework.beans.BeanUtils;

import com.lotus.check_point.dto.CheckPointDto;
import com.lotus.check_point.entity.CheckPointEntity;

public class CheckPointMapper {
    public static CheckPointDto toMapperDto(CheckPointEntity checkPointEntity) {
        CheckPointDto checkPointDto = new CheckPointDto();
        BeanUtils.copyProperties(checkPointEntity, checkPointDto);
        return checkPointDto;
    }
}