package com.lotus.check_point.repository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.lotus.check_point.entity.UserEntity;
import jakarta.persistence.LockModeType;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM UserEntity u WHERE u.id = :userId")
    Optional<UserEntity> findUserForUpdate(Long userId);

    Page<UserEntity> findAll(Pageable pageable);

    UserEntity findByUsername(String username);
}

