// 松原編集
    // ApiたたかれたらリマインドDBからデータを取ってくる処理これはサービス
    // サービスを使う処理をかく
    // リマインドからとってくるってやつはUserのログインのやつの簡易バージョンをとってくればいいけど
    // 特定の時刻を取ってくるってのが難しい。 

package com.daily_app.demo.Controller;

import com.daily_app.demo.Dto.Response.RemindResponseDto;
import com.daily_app.demo.Entity.Remind;
import com.daily_app.demo.Service.RemindService; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/remind") 
@CrossOrigin(origins = "http://localhost:5173") 
public class RemindController {

    @Autowired
    private RemindService remindService;

    /**
     * API: ログインしているユーザーの通知一覧を取得する
     * URL: GET /api/reminds/{user_id}
     */
    @GetMapping("/{user_id}")
    public ResponseEntity<List<RemindResponseDto>> getUserReminds(@PathVariable("user_id") Long userId) {
        System.out.println("ユーザーID: " + userId + " が通知を取りにきました。");

        List<RemindResponseDto> reminds = remindService.getRemindsByUserId(userId);

        return ResponseEntity.ok(reminds);
    }
}