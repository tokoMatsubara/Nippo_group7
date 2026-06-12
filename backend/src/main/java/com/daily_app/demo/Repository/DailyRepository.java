package com.daily_app.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.daily_app.demo.Entity.Daily;
import com.daily_app.demo.Entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyRepository extends JpaRepository<Daily, Integer> {

    // 「このユーザー」が「この日付」に書いた日報を1件取ってくる特注ボタン
    Optional<Daily> findByUserAndDailyDate(User user, LocalDate dailyDate);

    List<Daily> findByUser_UserIdAndDailyDateBetween(
            Integer userId, LocalDate startDate, LocalDate endDate);
    boolean existsByUserAndDailyDate(User user, LocalDate starDate);

    //↓非同期処理でも安全にDaily + DailyDetailを扱うための必須対策らしい
    //↓これないと、LazyInitializationExceptionが出てなんか落ちる
    @Query("""
            select d from Daily d
            join fetch d.dailyDetails
            where d.user.userId = :userId
            and d.dailyDate between :startDate and :endDate
            """)
    List<Daily> findWeeklyWithDetails(
            @Param("userId") Integer userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
