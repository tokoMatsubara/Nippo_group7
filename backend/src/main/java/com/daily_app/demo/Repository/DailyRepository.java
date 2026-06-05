package com.daily_app.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daily_app.demo.Entity.Daily;
import com.daily_app.demo.Entity.User;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DailyRepository extends JpaRepository<Daily, Integer> {
    
    // 「このユーザー」が「この日付」に書いた日報を1件取ってくる特注ボタン
    Optional<Daily> findByUserAndDailyDate(User user, LocalDate dailyDate);
}
