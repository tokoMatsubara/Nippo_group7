// 松原編集
// ApiたたかれたらリマインドDBからデータを取ってくる処理これはサービス
// サービスを使う処理をかく
// リマインドからとってくるってやつはUserのログインのやつの簡易バージョンをとってくればいいけど
// 特定の時刻を取ってくるってのが難しい。 

package com.daily_app.demo.Controller;

import com.daily_app.demo.Dto.Response.RemindIsReadDto;
import com.daily_app.demo.Dto.Response.RemindResponseDto;
import com.daily_app.demo.Service.RemindService;
import com.daily_app.demo.config.CustomUserDetails;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RemindController {

    private final RemindService remindService;

    public RemindController(RemindService remindService){
        this.remindService = remindService;
    }

    /**
     * API: ログインしているユーザーの通知一覧を取得する
     * URL: GET /api/reminds/{user_id}
     */
    @GetMapping("/remind")
    public ResponseEntity<List<RemindResponseDto>> getUserReminds(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Integer userId = userDetails.getId();
        System.out.println("ユーザーID: " + userId + " が通知を取りにきました。");

        List<RemindResponseDto> reminds = remindService.getRemindsByUserId(userId);

        return ResponseEntity.ok(reminds);
    }

    @GetMapping("/remind/is_read")
    public ResponseEntity<RemindIsReadDto> getMethodName(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return remindService.remindIsRead(userDetails.getId());
    }

}