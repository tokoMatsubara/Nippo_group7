package com.daily_app.demo.Service;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import com.daily_app.demo.Dto.RemindSettingDto;
import com.daily_app.demo.Dto.Request.LoginRequestDto;
import com.daily_app.demo.Dto.Request.UserCreateRequestDto;
import com.daily_app.demo.Dto.Response.LoginResponseDto;
import com.daily_app.demo.Entity.User;
import com.daily_app.demo.Repository.UserRepository;

@Controller
public class UserService {

   
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ユーザー登録ロジック=================================================================

    public ResponseEntity<Map<String, Object>> createUser(UserCreateRequestDto requestDto) {
        Map<String, Object> response = new HashMap<>();

        // 【追加】同じメールアドレスが既に登録されていないかチェック
        Optional<User> existingUser = userRepository.findByMailAddress(requestDto.getMailAddress());
        if (existingUser.isPresent()) {
            response.put("status", "error");
            response.put("message", "このメールアドレスは既に登録されています。");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        String hashedPassword = passwordEncoder.encode(requestDto.getPassword());

        // 2. 画面から届いたDTOのデータを使って、Entity(User)のインスタンスを作成
        // リマインド初期値：status=false, time=null (User.javaのコンストラクタを利用)
        User newUser = new User(
                requestDto.getUserName(),
                hashedPassword, 
                requestDto.getMailAddress(),
                false,
                java.time.LocalTime.of(9, 0));

        // 3. データベースに保存！
        // saveメソッドを実行すると、自動発番されたuserIdが含まれた状態のEntityが返ってきます
        User savedUser = userRepository.save(newUser);

        // 4. フロント(React)へ返すレスポンスデータを作成
        response.put("status", "success");
        response.put("message", "ユーザー登録が完了しました");
        response.put("userId", savedUser.getUserId()); // DBで自動採番された本物のIDを返す

        return ResponseEntity.ok(response);
    }

    // ログインロジック=================================================================

    public ResponseEntity<LoginResponseDto> login(LoginRequestDto requestDto) {

        // 1. 入力されたメールアドレスでDBを検索
        Optional<User> userOpt = userRepository.findByMailAddress(requestDto.getMailAddress());

        // 2. ユーザーが存在するかチェック
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // 3. パスワードが一致するかチェック
            if (passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
                // 認証成功：LoginResponseDtoを生成して返却
                LoginResponseDto response = new LoginResponseDto(
                        true,
                        "ログインに成功しました",
                        user.getUserName());
                return ResponseEntity.ok(response);
            }
        }

        // 4. ユーザーが存在しない、またはパスワード不一致の場合（認証失敗）
        // モックでは常にtrueを返していましたが、失敗時はfalseを返すようにします
        LoginResponseDto response = new LoginResponseDto(false, "メールアドレスまたはパスワードが間違っています");

        // 401 Unauthorized（認証エラー）のステータスコードで返すのが一般的です
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // リマインド設定ロジック=================================================================

    public ResponseEntity<Map<String, Object>> updateRemindSettings(User user, RemindSettingDto request) {
        System.out.println("ユーザーID: " + user.getUserId() + " のリマインド設定を更新します");
        Map<String, Object> response = new HashMap<>();
        try {
            user.setRemindStatus(request.getRemindStatus());
            user.setRemindTime(request.getRemindTime());
            userRepository.save(user);

            response.put("status", "success");
            response.put("message", "リマインド設定を登録しました");
      
            return ResponseEntity.ok(response);
            
        }catch(Exception e){
            System.out.println(e.getMessage());
            response.put("status", "error");
            response.put("message", "リマインド設定が失敗しました");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<RemindSettingDto> getRemindSetting(User user) {
        boolean remindStatus = user.getRemindStatus();
        LocalTime remindTime = user.getRemindTime();
        RemindSettingDto remindSettingDto = new RemindSettingDto(remindStatus, remindTime);
        return ResponseEntity.ok(remindSettingDto);
    }

    // ユーザーネーム・メアド・パスワードが画面上で変更できるようにする
    public ResponseEntity<Map<String, Object>> updateProfile(User user, UserCreateRequestDto requestDto) {


        // ちゃんとデータ渡せてるか見るための
        System.out.println("=== updateProfile開始 ===");
        System.out.println("userId: " + user.getUserId());
        System.out.println("userName: " + requestDto.getUserName());
        System.out.println("mailAddress: " + requestDto.getMailAddress());


        Map<String, Object> response = new HashMap<>();

        try {

            // メールアドレス重複チェック
        if (requestDto.getMailAddress() != null &&
            !requestDto.getMailAddress().isBlank()) {

            Optional<User> existingUser =
                userRepository.findByMailAddress(requestDto.getMailAddress());

            if (existingUser.isPresent() &&
                !existingUser.get().getUserId().equals(user.getUserId())) {

                response.put("status", "error");
                response.put("message", "このメールアドレスは既に使用されています。");

                return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
            }
        }


            user.setUserName(requestDto.getUserName());

            if (requestDto.getMailAddress() != null) {
                user.setMailAddress(requestDto.getMailAddress());
            }

            if (requestDto.getPassword() != null &&
                    !requestDto.getPassword().isBlank()) {

                String hashedPassword = passwordEncoder.encode(requestDto.getPassword());

                user.setPassword(hashedPassword);
            }
            
            System.out.println("保存前");
            System.out.println(user.getUserName());
            System.out.println(user.getMailAddress());

            userRepository.save(user);

            System.out.println("保存後");
            System.out.println(user.getUserName());
            System.out.println(user.getMailAddress());



            response.put("status", "success");
            response.put("message", "ユーザー名を更新しました");

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            response.put("status", "error");
            response.put("message", "ユーザー名の更新に失敗しました");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

  

   
}
