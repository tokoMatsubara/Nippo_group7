// 松原 編集した（DB保存ロジック合流版）
package com.daily_app.demo.Controller;

import com.daily_app.demo.Dto.Request.UserCreateRequestDto;
import com.daily_app.demo.Dto.Request.LoginRequestDto; 
import com.daily_app.demo.Dto.Response.LoginResponseDto;
import com.daily_app.demo.Entity.User;
import com.daily_app.demo.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    // 1. 作成したUserRepositoryをインジェクション（読み込み）します
    @Autowired
    private UserRepository userRepository;

    /**
     * API-006: ユーザー登録
     * URL: POST /api/create
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody UserCreateRequestDto requestDto) {
        
        System.out.println("--- ユーザー登録 本稼働 ---");
        Map<String, Object> response = new HashMap<>();

        // 【追加】同じメールアドレスが既に登録されていないかチェック
        Optional<User> existingUser = userRepository.findByMailAddress(requestDto.getMailAddress());
        if (existingUser.isPresent()) {
            response.put("status", "error");
            response.put("message", "このメールアドレスは既に登録されています。");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // 2. 画面から届いたDTOのデータを使って、Entity(User)のインスタンスを作成
        // リマインド初期値：status=false, time=null (User.javaのコンストラクタを利用)
        User newUser = new User(
            requestDto.getUserName(),
            requestDto.getPassword(), // ※将来的に暗号化推奨
            requestDto.getMailAddress(),
            false,
            java.time.LocalTime.of(9, 0)
        );

        // 3. データベースに保存！
        // saveメソッドを実行すると、自動発番されたuserIdが含まれた状態のEntityが返ってきます
        User savedUser = userRepository.save(newUser);

        // 4. フロント(React)へ返すレスポンスデータを作成
        response.put("status", "success");
        response.put("message", "ユーザー登録が完了しました");
        response.put("userId", savedUser.getUserId()); // DBで自動採番された本物のIDを返す

        return ResponseEntity.ok(response);
    }

    /**
     * API-007: ログイン認証
     * URL: POST /api/login
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto) {
        
        System.out.println("--- ログイン認証 本稼働 ---");

        // 1. 入力されたメールアドレスでDBを検索
        Optional<User> userOpt = userRepository.findByMailAddress(requestDto.getMailAddress());

        // 2. ユーザーが存在するかチェック
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            
            // 3. パスワードが一致するかチェック
            if (user.getPassword().equals(requestDto.getPassword())) {
                // 認証成功：LoginResponseDtoを生成して返却
                LoginResponseDto response = new LoginResponseDto(true, "ログインに成功しました");
                return ResponseEntity.ok(response);
            }
        }

        // 4. ユーザーが存在しない、またはパスワード不一致の場合（認証失敗）
        // モックでは常にtrueを返していましたが、失敗時はfalseを返すようにします
        LoginResponseDto response = new LoginResponseDto(false, "メールアドレスまたはパスワードが間違っています");
        
        // 401 Unauthorized（認証エラー）のステータスコードで返すのが一般的です
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }


    /**
     * API-009: リマインド設定登録
     * URL: POST /api/remind/settings/{user_id}
     */
    @PostMapping("/remind/settings/{user_id}")
    public ResponseEntity<Map<String, Object>> updateRemindSettings(@PathVariable("user_id") Long userId) {
        System.out.println("ユーザーID: " + userId + " のリマインド設定を更新します（※ここはまだモック状態です）");

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "ユーザーID: " + userId + " のリマインド設定を登録しました（モック）");

        return ResponseEntity.ok(response);
    }
}






// package com.daily_app.demo.Controller;

// import com.daily_app.demo.Dto.Request.UserCreateRequestDto;
// import com.daily_app.demo.Dto.Response.LoginResponseDto;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import java.util.HashMap;
// import java.util.Map;
// import com.daily_app.demo.Dto.Request.LoginRequestDto; 

// @RestController
// @RequestMapping("/api")
// public class UserController {

//     /**
//      * API-006: ユーザー登録
//      * URL: POST /api/create
//      */
//     @PostMapping("/create")
//     public ResponseEntity<Map<String, Object>> createUser(@RequestBody UserCreateRequestDto requestDto) {
        
//         System.out.println("--- ユーザー登録モック受付 ---");
//         System.out.println("名前: " + requestDto.getUserName());
//         System.out.println("メール: " + requestDto.getMailAddress());
//         System.out.println("パスワード: " + requestDto.getPassword());

//         // ダミーのレスポンスデータを作成
//         Map<String, Object> response = new HashMap<>();
//         response.put("status", "success");
//         response.put("message", "ユーザー登録が完了しました（モック）");
//         response.put("userId", 12345);

//         return ResponseEntity.ok(response);
//     }

//     /**
//      * API-007: ログイン認証
//      * URL: POST /api/login
//      */
//     @PostMapping("/login")
//     public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto) {
        
//         System.out.println("--- ログイン認証モック受付 ---");
//         System.out.println("メールアドレス: " + requestDto.getMailAddress());
//         System.out.println("パスワード: " + requestDto.getPassword());

//         // ダミーのレスポンスデータとして、作成したLoginResponseDtoを返します
//         // パターン1（record）の場合の作り方：
//         LoginResponseDto response = new LoginResponseDto(true, "ログインに成功しました（モック）");

//         return ResponseEntity.ok(response);
//     }


//     /**
//      * API-009: リマインド設定登録
//      * URL: POST /api/remind/settings/{user_id}
//      */
//     @PostMapping("/remind/settings/{user_id}")
//     public ResponseEntity<Map<String, Object>> updateRemindSettings(@PathVariable("user_id") Long userId) {
//         System.out.println("ユーザーID: " + userId + " のリマインド設定を更新します");

//         Map<String, Object> response = new HashMap<>();
//         response.put("status", "success");
//         response.put("message", "ユーザーID: " + userId + " のリマインド設定を登録しました（モック）");

//         return ResponseEntity.ok(response);
//     }

    
// }