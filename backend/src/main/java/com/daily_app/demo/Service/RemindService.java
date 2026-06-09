// 松原編集
package com.daily_app.demo.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daily_app.demo.Entity.User;
import com.daily_app.demo.Entity.Remind; 
import com.daily_app.demo.Repository.RemindRepository;
import com.daily_app.demo.Dto.Response.RemindIsReadDto;
import com.daily_app.demo.Dto.Response.RemindResponseDto;

@Service
public class RemindService {

    @Autowired
    private RemindRepository remindRepository;

    @Autowired
    private RemindMessageGenerator remindMessageGenerator;

    /**
     * 該当するユーザー全員分、リマインド保管箱にデータを詰めてクレーンで保存する
     */
    @Transactional
    public void createReminds(List<User> targetUsers) {

        
        for (User user : targetUsers) {

            // RemindMessageGenreratorからもらったmessageを使う。関数を作る
            String message = remindMessageGenerator.generateMessage(user);

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
     * 指定されたユーザーIDの通知を、Dtoの形に詰め替えて返す
     */
    @Transactional
    public List<RemindResponseDto> getRemindsByUserId(Integer userId) {
        // 1. DBから生のデータを取ってくる
        List<Remind> rawReminds = remindRepository.findByUser_UserId(userId);

        for (Remind remind : rawReminds){

            remind.setIsRead(true);
        }
        // 2. 生のRemindから、UserIdとContentだけを抜き出してDtoに詰め替える
        return rawReminds.stream()
            .map(remind -> new RemindResponseDto(
                remind.getUser().getUserId(), // UserからIDだけを引っこ抜く
                remind.getRemindContent()     // メッセージ内容
            ))
            .collect(Collectors.toList()); // リストにして返す
    }


    public ResponseEntity<RemindIsReadDto> remindIsRead(Integer userId){
        List<Remind> reminds = remindRepository.findByUser_UserId(userId);
        boolean allRead = reminds.stream().allMatch(Remind::getIsRead);
        return ResponseEntity.ok(new RemindIsReadDto(allRead));
    }
}