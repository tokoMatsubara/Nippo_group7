package com.daily_app.demo.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daily_app.demo.Entity.User;
import com.daily_app.demo.Entity.Remind; // 松原さんが作った保管箱
import com.daily_app.demo.Repository.RemindRepository;

@Service
public class RemindService {

    @Autowired
    private RemindRepository remindRepository;

    /**
     * 該当するユーザー全員分、リマインド保管箱にデータを詰めてクレーンで保存する
     */
    @Transactional
    public void createReminds(List<User> targetUsers) {
        
        for (User user : targetUsers) {

            String message = "リマインドの時間です。今日の日報を提出しましょう！";

            // 追加したボタンを押す。すでに同じメッセージがあればスキップ
            if(remindRepository.existsByUserAndRemindContent(user, message)){
                continue;
            }

            // まだなければ新しく作って保存
            Remind remind = new Remind(user, message);

            // 3. RemindRepositoryでDBに保存
            remindRepository.save(remind);
        }
    }
}