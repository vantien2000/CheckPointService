package com.lotus.check_point.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "sys_checkpoint")
@DynamicInsert
@DynamicUpdate
public class CheckPointEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    public UserEntity users;

    @Column(name = "check_date", columnDefinition = "Timestamp comment 'Tên đầy đủ'", nullable = true, unique = false)
    public LocalDateTime checkDate;

    @Column(name = "status", columnDefinition = "varchar(10) default 'CHEKED' comment 'CHEKED/UNCHEKED'", nullable = true, unique = false)
    public String status;

    @Column(name = "lotus", columnDefinition = "int comment 'điểm check point'", nullable = true, unique = false)
    public int lotus;

    @Column(name = "created_at", columnDefinition = "TimeStamp comment 'Thời gian tạo'", nullable = true, unique = false)
    public LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TimeStamp comment 'Thời gian cập nhật'", nullable = true, unique = false)
    public LocalDateTime updatedAt;
}
