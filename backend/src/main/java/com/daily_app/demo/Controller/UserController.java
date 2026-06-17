// 松原 編集
package com.daily_app.demo.Controller;

import com.daily_app.demo.Dto.Request.UserInfoRequestDto;
import com.daily_app.demo.Dto.Request.UsernameUpdateRequestDto;
import com.daily_app.demo.Dto.RemindSettingDto;
import com.daily_app.demo.Dto.Request.EmailUpdateRequestDto;
import com.daily_app.demo.Dto.Request.EmailUpdateRequestDto;
import com.daily_app.demo.Dto.Request.LoginRequestDto;
import com.daily_app.demo.Dto.Request.PasswordUpdateReqeustDto;
import com.daily_app.demo.Dto.Response.LoginResponseDto;
import com.daily_app.demo.Dto.Response.UserUpdateResponseDto;
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
    @PutMapping("/user/username")
    public ResponseEntity<UserUpdateResponseDto> updateUserName(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UsernameUpdateRequestDto request) {

        System.out.println("=== /api/user/username 到達 ===");
        System.out.println("ログインユーザーID: " + userDetails.getId());

        try{
            ResponseEntity<UserUpdateResponseDto> response =  userService.updateUsername(userDetails.getUser(), request.getUserName());
            return response;
        }catch(Exception e){
            UserUpdateResponseDto responseDto = new UserUpdateResponseDto(false, "ユーザーネームの変更に失敗しました");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }
    }

    @PutMapping("/user/email")
    public ResponseEntity<UserUpdateResponseDto> putMethodName(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody EmailUpdateRequestDto request,
            HttpServletResponse response) {

        System.out.println("=== /api/user/email 到達 ===");
        System.out.println("ログインユーザーID: " + userDetails.getId());
        
        try{
            ResponseEntity<UserUpdateResponseDto> responseData = userService.updateEmail(userDetails.getUser(), request.getMailAddress());
            ResponseCookie token = refleshAccessToken(userDetails.getUsername());
            response.addHeader(HttpHeaders.SET_COOKIE, token.toString());
            return responseData;
        }catch(Exception e){
            System.err.println(e.getMessage());
            UserUpdateResponseDto responseDto = new UserUpdateResponseDto(false, "メールアドレスの変更に失敗しました");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }
    }

    @PutMapping("/user/password")
    public ResponseEntity<UserUpdateResponseDto> putMethodName(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody PasswordUpdateReqeustDto request) {

        System.out.println("=== /api/user/password 到達 ===");
        System.out.println("ログインユーザーID: " + userDetails.getId());
        
        try{
            ResponseEntity<UserUpdateResponseDto> response = 
                userService.updatePassword(
                    userDetails.getUser(), 
                    request.getCurrentPassword(), 
                    request.getNewPassword());
            
            return response;
        }catch(Exception e){
            System.err.println(e.getMessage());
            UserUpdateResponseDto responseDto = new UserUpdateResponseDto(false, "パスワードの変更に失敗しました");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
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