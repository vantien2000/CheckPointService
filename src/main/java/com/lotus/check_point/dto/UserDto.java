package com.lotus.check_point.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserDto {
 
    public String fullName;

    public String address;

    public String phoneNumber;

    public String username;

    public int lotusAll;

    public LocalDateTime createdAt;

    public LocalDateTime updatedAt;
}
