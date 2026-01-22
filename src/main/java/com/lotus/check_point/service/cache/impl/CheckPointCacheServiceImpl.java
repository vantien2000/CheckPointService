package com.lotus.check_point.service.cache.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.lotus.check_point.cache.RedisService;
import com.lotus.check_point.constain.ScoreConstant;
import com.lotus.check_point.distributed.RedissonDistributedLocker;
import com.lotus.check_point.distributed.RedissonDistributedService;
import com.lotus.check_point.entity.CheckPointEntity;
import com.lotus.check_point.entity.UserEntity;
import com.lotus.check_point.enums.StatusEnums;
import com.lotus.check_point.repository.CheckPointRepository;
import com.lotus.check_point.repository.UserRepository;
import com.lotus.check_point.service.cache.CheckPointCacheService;

import jakarta.transaction.Transactional;

@Service
public class CheckPointCacheServiceImpl implements CheckPointCacheService {
    @Autowired
    private RedisService redisService;
    @Autowired
    private RedissonDistributedService redissonDistributedService;
    @Autowired
    private CheckPointRepository checkPointRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    @Scheduled(cron = "0 0 9,10,11,19,20,21 * * *") 
    public CheckPointEntity checkPointDaily(Long userId, boolean checked) {
        if (!checked) return null; // chạy job để câp nhật lại ngày chưa checked
        CheckPointEntity checkPointEntity = checkPointRepository.findUserIdAndStatusAndCheckDate(userId, StatusEnums.valueOf("CHECKED").toString(), LocalDateTime.now());
        if (checkPointEntity != null) {
            return checkPointEntity;
        }
        return getFromDistributedCache(userId);
    }

    @Override
    @Scheduled(cron = "0 0 22 * * *")
    public void unCheckPointDaily() {
        List<CheckPointEntity> checkPointEntityLst = checkPointRepository.findAllStatusAndCheckDate(StatusEnums.valueOf("CHECKED").toString(), LocalDateTime.now());

        if (checkPointEntityLst == null) return;
        if (checkPointEntityLst != null && checkPointEntityLst.size() == 0) return;

        for (CheckPointEntity unEntity : checkPointEntityLst) {
            unEntity.setCheckDate(LocalDateTime.now());
            unEntity.setStatus(StatusEnums.valueOf("UNCHECKED").toString());
            unEntity.setCreatedAt(LocalDateTime.now());
        }
        checkPointRepository.saveAll(checkPointEntityLst);
    } 

    public CheckPointEntity getFromDistributedCache(Long userId) {
        RedissonDistributedLocker dLock = redissonDistributedService.getDistributedLocker("LOCK_CHECK_POINT");
        try {
            // nếu chưa có trong database thử kiểm tra trong cache có chưa
            CheckPointEntity checkPointEntity = redisService.getObject(genCacheKey(userId), CheckPointEntity.class);
            if (checkPointEntity != null) throw new RuntimeException("Ngày hôm nay đã tích điểm rồi");

            boolean isLocked = dLock.tryLock(5, 10, TimeUnit.SECONDS); // luồng khác đợi 5s để nhận khóa, và sau l0s lock tự xóa
            if (!isLocked) {// chưa locked
                return checkPointEntity;
            }
            checkPointEntity = changeCheckpoint(userId);
            redisService.setObject(genCacheKey(userId), checkPointEntity);
            return checkPointEntity;

        } catch (Exception e) {
            dLock.unlock(); // buộc unlock bất kể xử lý xong hay chưa
            throw new RuntimeException(e.getMessage());
        } finally {
            if (dLock.isLocked()) {
                dLock.unlock();
            }
        }
    }

    // code nguu viet lai
    @Transactional
    public CheckPointEntity changeCheckpoint(Long userId) {
        // 2. Lấy dữ liệu điểm
        int countChecked = checkPointRepository.GetCountCheckedInMonth(
            userId, 
            StatusEnums.CHECKED.name(), 
            LocalDateTime.now()
        );
        
        int scoreInToday = ScoreConstant.SCORE.getOrDefault(countChecked + 1, 0);
        int oldScore = userRepository.findById(userId).orElse(null).getLotusAll();
        // dùng cas để tránh spam
        UserEntity userEntity = userRepository.addLotusAllUser(userId, scoreInToday, oldScore);

        // 4. Lưu log Checkpoint
        CheckPointEntity checkPointEntity = new CheckPointEntity();
        checkPointEntity.setUsers(userEntity);
        checkPointEntity.setLotus(scoreInToday);
        checkPointEntity.setCheckDate(LocalDateTime.now());
        checkPointEntity.setStatus(StatusEnums.CHECKED.name());
        checkPointEntity.setCreatedAt(LocalDateTime.now());
        
        return checkPointRepository.save(checkPointEntity);
    }

    private String genCacheKey(Long userId) {
        if (Long.valueOf(userId) == 0) {
            return null;
        }
        return "CHECK_POINT_USER:"+userId;
    }


}
