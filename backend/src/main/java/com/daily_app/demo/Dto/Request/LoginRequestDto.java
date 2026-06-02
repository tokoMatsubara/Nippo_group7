// 松原編集
package com.daily_app.demo.Dto.Request;

/**
 * ログインリクエスト用のDTO（通常のクラス版）
 */
public class LoginRequestDto {

    private String mailAddress; // コントローラー側と合わせて「dd」にしています
    private String password;

    // デフォルトコンストラクタ
    public LoginRequestDto() {
    }

    // 全フィールドを初期化するコンストラクタ
    public LoginRequestDto(String mailAddress, String password) {
        this.mailAddress = mailAddress;
        this.password = password;
    }

    // Getter / Setter
    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}