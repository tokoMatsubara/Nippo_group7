package com.daily_app.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daily_app.demo.Entity.Remind;
import com.daily_app.demo.Entity.User;

public interface RemindRepository extends JpaRepository<Remind, Integer>{

    // 🌟特注ボタン：「このユーザー」に「この内容」のリマインドがすでに存在するかチェックする
    boolean existsByUserAndRemindContent(User user, String remindContent);
    
}
