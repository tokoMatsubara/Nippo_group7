package com.daily_app.demo.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

import com.daily_app.demo.Dto.Request.LoginRequestDto;
import com.daily_app.demo.Dto.Request.UserCreateRequestDto;
import com.daily_app.demo.Dto.Response.LoginResponseDto;
import com.daily_app.demo.Entity.User;
import com.daily_app.demo.Repository.UserRepository;

@Controller
public class UserService {

    // 1. 作成したUserRepositoryをインジェクション（読み込み）します
    @Autowired
    private UserRepository userRepository;
    //パスワードのハッシュ化
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    //ユーザー登録ロジック=================================================================

    public ResponseEntity<Map<String, Object>> createUser(UserCreateRequestDto requestDto){
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
                hashedPassword, // ※将来的に暗号化推奨
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


    //ログインロジック=================================================================

    public ResponseEntity<LoginResponseDto> login(LoginRequestDto requestDto) {

        System.out.println("--- ログイン認証 本稼働 ---");

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
                        user.getUserId(),
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

    //リマインド設定ロジック=================================================================

    public ResponseEntity<Map<String, Object>> updateRemindSettings(Integer userId) {
        System.out.println("ユーザーID: " + userId + " のリマインド設定を更新します（※ここはまだモック状態です）");

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "ユーザーID: " + userId + " のリマインド設定を登録しました（モック）");

        return ResponseEntity.ok(response);
    }
}
