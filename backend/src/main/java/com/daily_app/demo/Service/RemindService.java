// 松原編集
package com.daily_app.demo.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daily_app.demo.Entity.User;
import com.daily_app.demo.Entity.Remind; 
import com.daily_app.demo.Repository.RemindRepository;
import com.daily_app.demo.Dto.Response.RemindResponseDto;

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

    /**
     * 🌟【修正】指定されたユーザーIDの通知を、Dtoの形に詰め替えて返す
     */
    @Transactional(readOnly = true)
    public List<RemindResponseDto> getRemindsByUserId(Long userId) {
        // 1. DBから生のデータを取ってくる
        List<Remind> rawReminds = remindRepository.findByUser_UserId(userId);

        // 2. 生のRemindから、UserIdとContentだけを抜き出してDtoに詰め替える
        return rawReminds.stream()
            .map(remind -> new RemindResponseDto(
                remind.getUser().getUserId(), // UserからIDだけを引っこ抜く
                remind.getRemindContent()     // メッセージ内容
            ))
            .collect(Collectors.toList()); // リストにして返す
    }
}