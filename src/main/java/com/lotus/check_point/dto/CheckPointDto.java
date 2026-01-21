package com.lotus.check_point.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CheckPointDto {
    public Long Id;
    public LocalDateTime checkDate;
    public String status;
    public String lotus;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
}
