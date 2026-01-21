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

import com.lotus.check_point.dto.CheckPointDto;
import com.lotus.check_point.dto.request.CheckPointRequest;
import com.lotus.check_point.dto.response.base.ApiResponse;
import com.lotus.check_point.entity.CheckPointEntity;
import com.lotus.check_point.mapper.CheckPointMapper;
import com.lotus.check_point.service.CheckPointService;
import com.lotus.check_point.service.cache.CheckPointCacheService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("v1/api")
public class CheckPointController {
    @Autowired
    private CheckPointCacheService checkPointCacheService;

    @Autowired
    private CheckPointService checkPointService;

    @PostMapping("/checkpoint")
    public ResponseEntity checkPointDaily(@RequestBody CheckPointRequest checkPointRequest) {
        try {
            CheckPointEntity checkPointEntity = checkPointCacheService.checkPointDaily(checkPointRequest.userId, checkPointRequest.checkPoint);
            CheckPointDto checkPointDto = CheckPointMapper.toMapperDto(checkPointEntity);
            return new ResponseEntity<>(ApiResponse.success(checkPointDto), HttpStatus.OK);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @GetMapping("/history/checkpoint")
    public Page<CheckPointDto> getHistoryCheckPoint(
        @RequestParam Long userId,
        @RequestParam int page,
        @RequestParam int size,
        @RequestParam(defaultValue = "id") String sort,
        @RequestParam(defaultValue = "desc") String direction) {
        try {
            Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            Sort sortBy = Sort.by(sortDirection, sort);
            Pageable pageable = PageRequest.of(page, size, sortBy);
            return checkPointService.getAllCheckpoint(userId, pageable).map(entity -> CheckPointMapper.toMapperDto(entity));
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @GetMapping("/checkpoint/plus")
    public Page<CheckPointDto> checkPointDaily(@RequestParam Long userId,
            @RequestParam String status,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "desc") String direction) {
        try {
            Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            Sort sortBy = Sort.by(sortDirection, sort);
            Pageable pageable = PageRequest.of(page, size, sortBy);
            return checkPointService.getAllCheked(userId, status, pageable).map(entity -> CheckPointMapper.toMapperDto(entity));
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
