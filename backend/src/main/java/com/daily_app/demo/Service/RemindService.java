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
            
            // 1. 【重複防止】もしすでに今日この人にリマインドを送っていたらスキップ
            // ※今日すでに存在するかどうかのチェック方法は、後ほどRepositoryを作るときに合わせましょう！
            // if (remindRepository.existsByUserAnd... ) { continue; }

            // 2. 【詰め替え】松原さんが作ってくれたコンストラクタ（2つの引数）をそのまま使う！
            String message = "リマインドの時間です。今日の日報を提出しましょう！";
            Remind remind = new Remind(user, message);

            // 3. 【保管】専用クレーン（RemindRepository）でデータベースの棚に保存
            remindRepository.save(remind);
        }
    }
}