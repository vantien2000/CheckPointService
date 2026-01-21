package com.lotus.check_point.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "sys_users")
@DynamicInsert
@DynamicUpdate
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "fullname", columnDefinition = "varchar(255) comment 'Tên đầy đủ'", nullable = true, unique = false)
    public String fullName;

    @Column(name = "address", columnDefinition = "varchar(255) comment 'Địa chỉ'", nullable = true, unique = false)
    public String address;

    @Column(name = "phone_number", columnDefinition = "varchar(255) comment 'số điện thoại'", nullable = true, unique = false)
    public String phoneNumber;

    @Column(name = "username", columnDefinition = "varchar(255) comment 'Tên đăng nhập'", nullable = false, unique = true)
    public String username;

    @Column(name = "password", columnDefinition = "varchar(255) comment 'Mật khẩu người dùng'", nullable = false, unique = false)
    public String password;

    @Column(name = "lotus_all", columnDefinition = "int default 0 comment 'Tổng điểm điểm danh'", nullable = true, unique = false)
    public int lotusAll;

    @Column(name = "created_at", columnDefinition = "Timestamp comment 'Thời gian tạo'", nullable = true, unique = false)
    public LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "Timestamp comment 'Thời gian cập nhật'", nullable = true, unique = false)
    public LocalDateTime updatedAt;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CheckPointEntity> checkPoint;
}
