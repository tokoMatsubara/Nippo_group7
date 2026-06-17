//トオコ編集中マジで難しい。いまはTransactionalのアノテーション付けたところ。
package com.daily_app.demo.Service;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daily_app.demo.Dto.RemindSettingDto;
import com.daily_app.demo.Dto.Request.LoginRequestDto;
import com.daily_app.demo.Dto.Request.UserInfoRequestDto;
import com.daily_app.demo.Dto.Response.LoginResponseDto;
import com.daily_app.demo.Dto.Response.UserUpdateResponseDto;
import com.daily_app.demo.Entity.User;
import com.daily_app.demo.Repository.UserRepository;

import io.micrometer.core.ipc.http.HttpSender.Response;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ユーザー登録ロジック=================================================================
    @Transactional
    public ResponseEntity<Map<String, Object>> createUser(UserInfoRequestDto requestDto) {
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

        // 新規登録時に画面からテーマカラーが送られてきていたらEntityにセットする
        if (requestDto.getUserTheme() != null && !requestDto.getUserTheme().isBlank()) {
            newUser.setUserTheme(requestDto.getUserTheme());
        }

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
                        user.getUserName(),
                        user.getUserTheme());

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
    @Transactional
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

        } catch (Exception e) {
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

    // ユーザーネーム・メアド・パスワードが画面上で変更できるようにする==========================
    @Transactional
    public ResponseEntity<UserUpdateResponseDto> updateUsername(User user, String newUsername) throws Exception{
        
        if(newUsername.isBlank()){
            UserUpdateResponseDto response = new UserUpdateResponseDto(false, "空白の入力です");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        user.setUserName(newUsername);
        userRepository.save(user);
        UserUpdateResponseDto response = new UserUpdateResponseDto(true, "ユーザーネームを更新しました", user.getUserName());
        return ResponseEntity.ok(response);

    }

    @Transactional
    public ResponseEntity<UserUpdateResponseDto> updateEmail(User user, String newEmail) throws Exception{
        if(newEmail.isBlank()){
            UserUpdateResponseDto response = new UserUpdateResponseDto(false, "空白の入力です");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Optional<User> existingUser = userRepository.findByMailAddress(newEmail);

        if (existingUser.isPresent() &&
                !existingUser.get().getUserId().equals(user.getUserId())) {
            
            UserUpdateResponseDto response = new UserUpdateResponseDto(false, "このメールアドレスは既に使用されています");


            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }

        user.setMailAddress(newEmail);
        userRepository.save(user);
        UserUpdateResponseDto response = new UserUpdateResponseDto(true, "メールアドレスを更新しました");
        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<UserUpdateResponseDto> updatePassword(User user, String currentPassword, String newPassword)
        throws Exception{
        if(newPassword.isBlank()){
            UserUpdateResponseDto response = new UserUpdateResponseDto(false, "空白の入力です");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if(!passwordEncoder.matches(currentPassword, user.getPassword())){
            UserUpdateResponseDto response = new UserUpdateResponseDto(false, "空白の入力です");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        String hashedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashedPassword);
        userRepository.save(user);
        UserUpdateResponseDto response = new UserUpdateResponseDto(true, "パスワードを変更しました");
        return ResponseEntity.ok(response);
    }


    @Transactional
    public ResponseEntity<LoginResponseDto> updateProfile(User user, UserInfoRequestDto requestDto)
        throws Exception {

        // ちゃんとデータ渡せてるか見るための
        System.out.println("=== updateProfile開始 ===");
        System.out.println("userId: " + user.getUserId());
        System.out.println("userName: " + requestDto.getUserName());
        System.out.println("mailAddress: " + requestDto.getMailAddress());
        System.out.println("userTheme: " + requestDto.getUserTheme());

        LoginResponseDto response = new LoginResponseDto();


        // メールアドレス重複チェック
        if (requestDto.getMailAddress() != null &&
                !requestDto.getMailAddress().isBlank()) {

            Optional<User> existingUser = userRepository.findByMailAddress(requestDto.getMailAddress());

            if (existingUser.isPresent() &&
                    !existingUser.get().getUserId().equals(user.getUserId())) {

                response.setSuccess(false);
                response.setMessage("このメールアドレスは既に使用されています。");

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(response);
            }
        }



        if (requestDto.getMailAddress() != null && !requestDto.getUserName().isBlank()) {
            user.setMailAddress(requestDto.getMailAddress());
        }

        // ユーザー名が届いている（nullでもなく空文字でもない）ときだけ上書きする
        if (requestDto.getUserName() != null && !requestDto.getUserName().isBlank()) {
            user.setUserName(requestDto.getUserName());
        }

        // メールアドレスが届いているときだけ上書きする（一応 .isBlank() も追加して安全に）
        if (requestDto.getMailAddress() != null && !requestDto.getMailAddress().isBlank()) {
            user.setMailAddress(requestDto.getMailAddress());
        }

        if (requestDto.getPassword() != null &&
                !requestDto.getPassword().isBlank()) {

            String hashedPassword = passwordEncoder.encode(requestDto.getPassword());

            user.setPassword(hashedPassword);
        }

        // Reactから新しいテーマカラーが届いていたらEntityに上書き保存する
        if (requestDto.getUserTheme() != null && !requestDto.getUserTheme().isBlank()) {
            user.setUserTheme(requestDto.getUserTheme());
        }

        System.out.println("保存前");

        userRepository.save(user);

        System.out.println("保存成功！");

        response.setSuccess(true);
        response.setMessage("ユーザー情報を更新しました");
        response.setUserName(user.getUserName());
        response.setUserTheme(user.getUserTheme());

        return ResponseEntity.ok(response);
    }

}
