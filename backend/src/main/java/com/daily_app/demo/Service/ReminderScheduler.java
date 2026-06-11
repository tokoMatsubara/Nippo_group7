// 松原編集
package com.daily_app.demo.Service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;


import com.daily_app.demo.Entity.User;
import com.daily_app.demo.Repository.UserRepository;


@Component
public class ReminderScheduler {

    private final UserRepository userRepository;
    private final RemindService remindService;


    public ReminderScheduler(UserRepository userRepository, RemindService remindService) {
        this.userRepository = userRepository;
        this.remindService = remindService;
    }

    private static final ZoneId ZONE = ZoneId.of("Asia/Tokyo");

    @Scheduled(cron = "0 * * * * *", zone = "Asia/Tokyo")
    @Transactional
    public void createReminders() {
        LocalDateTime nowDateTime = LocalDateTime.now(ZONE);
        
        LocalTime start = nowDateTime.toLocalTime().truncatedTo(ChronoUnit.MINUTES);
        LocalTime end = start.plusMinutes(1).minusNanos(1);
        
        // 通知オンオフのための機能の編集です。
        List<User> users = userRepository.findByRemindTimeBetweenAndRemindStatusTrue(start, end);

        if (!users.isEmpty()) {
            remindService.createReminds(users); //ここが9:30から一分前後じゃないと入らない
            
        }
        System.out.println("Schedulerがうごいているよ");
    }

}