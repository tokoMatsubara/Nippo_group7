// 松原 編集
package com.daily_app.demo.Controller;

import com.daily_app.demo.Dto.Request.UserInfoRequestDto;
import com.daily_app.demo.Dto.RemindSettingDto;
import com.daily_app.demo.Dto.Request.LoginRequestDto;
import com.daily_app.demo.Dto.Response.LoginResponseDto;
import com.daily_app.demo.Service.UserService;
import com.daily_app.demo.config.CustomUserDetails;
import com.daily_app.demo.config.JwtTokenProvider;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    private UserService userService;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;
    
    public UserController(
        UserService userService,
        AuthenticationManager authenticationManager,
        JwtTokenProvider tokenProvider){

        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    /**
     * API-006: ユーザー登録
     * URL: POST /api/create
     */
    @PostMapping("/auth/create")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody UserInfoRequestDto requestDto) {

        System.out.println("--- ユーザー登録 本稼働 ---");
        return userService.createUser(requestDto);
    }

    /**
     * API-007: ログイン認証
     * URL: POST /api/login
     */
    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto,
            HttpServletResponse response) {
        System.out.println("--- ログイン認証 本稼働 ---");
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getMailAddress(), requestDto.getPassword()));
            ResponseCookie cookie = refleshAccessToken(authentication.getName());

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            return userService.login(requestDto);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            LoginResponseDto loginResponse = new LoginResponseDto(false, "メールアドレスまたはパスワードが間違っています");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponse);
        }
    }

    /**
     * API-009: リマインド設定登録
     * URL: POST /api/remind/settings/{user_id}
     */
    @PutMapping("/remind/settings")
    public ResponseEntity<Map<String, Object>> updateRemindSettings(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody RemindSettingDto requestDto) {
        System.out.println("ユーザーID: " + userDetails.getId() + " のリマインド設定を更新します（※ここはまだモック状態です）");

        return userService.updateRemindSettings(userDetails.getUser(), requestDto);
    }

    @GetMapping("/remind/settings")
    public ResponseEntity<RemindSettingDto> getMethodName(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        return userService.getRemindSetting(userDetails.getUser());
    }

    // ユーザーネームが画面上で変更できるようにする
    @PutMapping("/user/profile")
    public ResponseEntity<LoginResponseDto> updateUserName(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UserInfoRequestDto requestDto,
            HttpServletResponse response) {

        System.out.println("=== /api/user/profile 到達 ===");
        System.out.println("ログインユーザーID: " + userDetails.getId());

        try {
            ResponseEntity<LoginResponseDto> profileResponse = userService.updateProfile(
                userDetails.getUser(),
                requestDto);
            System.out.println(userDetails.getUsername() + userDetails.getPassword());
            ResponseCookie cookie = refleshAccessToken(userDetails.getUsername());

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            return profileResponse;
        } catch (Exception e) {
            System.err.println(e.getMessage());

            LoginResponseDto loginResponse = new LoginResponseDto(false, "ユーザー情報の更新に失敗しました");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(loginResponse);
        }
    }

    private ResponseCookie refleshAccessToken(String mailAddress){
        String token = tokenProvider.generateToken(mailAddress);
        System.out.println(token);

        ResponseCookie cookie = ResponseCookie.from("accessToken", token)
                .httpOnly(true).secure(false).path("/")
                .maxAge(Duration.ofHours(20)).sameSite("Lax").build();
        return cookie;
    }


}