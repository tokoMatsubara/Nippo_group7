// 松原編集
package com.daily_app.demo.Controller;

import com.daily_app.demo.Dto.Request.UserCreateRequestDto; 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import com.daily_app.demo.Dto.Request.LoginRequestDto; 

@RestController
@RequestMapping("/api")
public class UserController {

    /**
     * API-006: ユーザー登録
     * URL: POST /api/create
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody UserCreateRequestDto requestDto) {
        
        System.out.println("--- ユーザー登録モック受付 ---");
        System.out.println("名前: " + requestDto.getUserName());
        System.out.println("メール: " + requestDto.getMailAddress());
        System.out.println("パスワード: " + requestDto.getPassword());

        // ダミーのレスポンスデータを作成
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "ユーザー登録が完了しました（モック）");
        response.put("userId", 12345);

        return ResponseEntity.ok(response);
    }

    /**
     * API-007: ログイン認証
     * URL: POST /api/login
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequestDto requestDto) {
        System.out.println("ログイン認証が呼び出されました");

        Map<String, Object> response = new HashMap<>();
        System.out.println("メールアドレス: " + requestDto.getMailAddress());
        System.out.println("パスワード: " + requestDto.password());

        response.put("status", "success");
        response.put("token", "dummy-jwt-token-xyz789");
        response.put("user_id", 12345);

        return ResponseEntity.ok(response);
    }

    /**
     * API-009: リマインド設定登録
     * URL: POST /api/remind/settings/{user_id}
     */
    @PostMapping("/remind/settings/{user_id}")
    public ResponseEntity<Map<String, Object>> updateRemindSettings(@PathVariable("user_id") Long userId) {
        System.out.println("ユーザーID: " + userId + " のリマインド設定を更新します");

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "ユーザーID: " + userId + " のリマインド設定を登録しました（モック）");

        return ResponseEntity.ok(response);
    }
}