// 松原 編集
package com.daily_app.demo.Controller;

import com.daily_app.demo.Dto.Request.UserCreateRequestDto;
import com.daily_app.demo.Dto.Request.LoginRequestDto;
import com.daily_app.demo.Dto.Response.LoginResponseDto;
import com.daily_app.demo.Repository.UserRepository;
import com.daily_app.demo.Service.CallLlmService;
import com.daily_app.demo.Service.UserService;
import com.daily_app.demo.config.CustomUserDetails;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/api")
// @CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;


    /**
     * API-006: ユーザー登録
     * URL: POST /api/create
     */
    @PostMapping("/auth/create")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody UserCreateRequestDto requestDto) {

        System.out.println("--- ユーザー登録 本稼働 ---");
        return userService.createUser(requestDto);
    }

    /**
     * API-007: ログイン認証
     * URL: POST /api/login
     */
    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response) {
        System.out.println("--- ログイン認証 本稼働 ---");
        try{
            Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(requestDto.getMailAddress(), requestDto.getPassword()));
            
            System.out.println(authentication.getName());

            ResponseCookie mail = ResponseCookie.from("email", authentication.getName())
            .httpOnly(true).secure(false).path("/")
            .maxAge(Duration.ofHours(1)).sameSite("Strict").build();
        
            response.addHeader(HttpHeaders.SET_COOKIE, mail.toString());
            return userService.login(requestDto);
        }catch(Exception e){
            System.err.println(e.getMessage());
            System.out.println("プリントしてます");
            LoginResponseDto loginResponse = new LoginResponseDto(false, "メールアドレスまたはパスワードが間違っています");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponse);
        }
    }

    /**
     * API-009: リマインド設定登録
     * URL: POST /api/remind/settings/{user_id}
     */
    @PostMapping("/remind/settings/")
    public ResponseEntity<Map<String, Object>> updateRemindSettings(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        System.out.println("ユーザーID: " + userDetails.getId() + " のリマインド設定を更新します（※ここはまだモック状態です）");
        
        return userService.updateRemindSettings(userDetails.getId());
    }
}