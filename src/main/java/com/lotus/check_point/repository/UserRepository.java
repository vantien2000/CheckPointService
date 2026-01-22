package com.lotus.check_point.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.lotus.check_point.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // @Lock(LockModeType.PESSIMISTIC_WRITE)
    // @Query("SELECT u FROM UserEntity u WHERE u.id = :userId")
    // Optional<UserEntity> findUserForUpdate(Long userId);

    Page<UserEntity> findAll(Pageable pageable);

    UserEntity findByUsername(String username);

    //sử dụng cas để optimize lock
    @Modifying
    @Query("UPDATE UserEntity u SET u.lotusAll = u.lotusAll + :newLotus " +
            "WHERE u.id = :userId AND u.lotusAll = :oldLotus")
    UserEntity addLotusAllUser(Long userId, int oldLotus, int newLotus);
}

