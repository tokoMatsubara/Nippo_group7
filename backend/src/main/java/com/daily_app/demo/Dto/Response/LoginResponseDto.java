package com.daily_app.demo.Dto.Response;


/**
 * ログインレスポンス用のDTO（通常のクラス版）
 */
public class LoginResponseDto {

    private Boolean success;
    private String message;

    // デフォルトコンストラクタ
    public LoginResponseDto() {
    }

    // 全フィールドを初期化するコンストラクタ
    public LoginResponseDto(Boolean success, String message) {
        this.success = success;
        this.message = message;
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
}