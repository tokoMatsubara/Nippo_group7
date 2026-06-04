// 松原編集
// 今藤 ログインレスポンスDTOにユーザーIDとユーザー名を追加

package com.daily_app.demo.Dto.Response;

/**
 * ログインレスポンス用のDTO
 */
public class LoginResponseDto {

    private Boolean success;
    private String message;

    // ★追加（ユーザー情報）
    private Integer userId;
    private String userName;

    // デフォルトコンストラクタ
    public LoginResponseDto() {
    }

    // 既存互換用（そのまま維持）
    public LoginResponseDto(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // ★追加コンストラクタ（推奨）
    public LoginResponseDto(Boolean success, String message, Integer userId, String userName) {
        this.success = success;
        this.message = message;
        this.userId = userId;
        this.userName = userName;
    }

    // Getter / Setter
    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // ★追加
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    // ★追加
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}