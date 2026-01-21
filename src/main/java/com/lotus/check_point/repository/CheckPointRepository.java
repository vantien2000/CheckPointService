package com.lotus.check_point.repository;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lotus.check_point.entity.CheckPointEntity;


import io.lettuce.core.dynamic.annotation.Param;
public interface CheckPointRepository extends JpaRepository<CheckPointEntity, Long> {
    //Tìm checkpoint theo userId, trạng thái và ngày check
    @Query("SELECT ck FROM CheckPointEntity ck WHERE ck.users.id = :userId AND ck.status = :status " +
           "AND FUNCTION('DATE', ck.checkDate) = FUNCTION('DATE', :checkDate)")
    public CheckPointEntity findUserIdAndStatusAndCheckDate(
        @Param("userId") Long userId, 
        @Param("status") String status, 
        @Param("checkDate") LocalDateTime checkDate);
    
    //Lấy số lần đẫ checkpoint trong tháng
    @Query("SELECT COUNT(1) FROM CheckPointEntity ck " +
           "WHERE ck.users.id = :userId " +
           "AND ck.status = :status " +
           "AND MONTH(ck.checkDate) = MONTH(:checkDate) " +
           "AND YEAR(ck.checkDate) = YEAR(:checkDate)")
    public int GetCountCheckedInMonth(
        @Param("userId") Long userId, 
        @Param("status") String status, 
        @Param("checkDate") LocalDateTime checkDate);

    //Lấy danh sách chưa checkpoint trong ngày
    @Query("SELECT ck FROM CheckPointEntity ck WHERE ck.status = :status " +
        "AND FUNCTION('DATE', ck.checkDate) = FUNCTION('DATE', :checkDate)")
    public List<CheckPointEntity> findAllStatusAndCheckDate(
        @Param("status") String status, 
        @Param("checkDate") LocalDateTime checkDate);

    // lấy danh sách checkpoint theo user
    public Page<CheckPointEntity> findByUsersId(Long userId, Pageable pageable);
    
    //Lấy danh sách đã checkpoint
    public Page<CheckPointEntity> findByUsersIdAndStatus(Long userId, String status, Pageable pageable);
}
