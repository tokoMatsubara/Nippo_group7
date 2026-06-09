// 松原 編集
package com.daily_app.demo.Controller;

import com.daily_app.demo.Dto.Request.UserCreateRequestDto;
import com.daily_app.demo.Dto.Request.LoginRequestDto;
import com.daily_app.demo.Dto.Response.LoginResponseDto;
import com.daily_app.demo.Repository.UserRepository;
import com.daily_app.demo.Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    // 1. 作成したUserRepositoryをインジェクション（読み込み）します
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * API-006: ユーザー登録
     * URL: POST /api/create
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody UserCreateRequestDto requestDto) {

        System.out.println("--- ユーザー登録 本稼働 ---");
        return userService.createUser(requestDto);
    }

    /**
     * API-007: ログイン認証
     * URL: POST /api/login
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto) {

        System.out.println("--- ログイン認証 本稼働 ---");
        return userService.login(requestDto);
    }

    /**
     * API-009: リマインド設定登録
     * URL: POST /api/remind/settings/{user_id}
     */
    @PostMapping("/remind/settings/{user_id}")
    public ResponseEntity<Map<String, Object>> updateRemindSettings(@PathVariable("user_id") Integer userId) {
        System.out.println("ユーザーID: " + userId + " のリマインド設定を更新します（※ここはまだモック状態です）");
        
        return userService.updateRemindSettings(userId);
    }
}