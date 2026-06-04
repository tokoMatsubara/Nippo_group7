// 松原編集
package com.daily_app.demo.Dto.Request;


public class LoginRequestDto {

    @com.fasterxml.jackson.annotation.JsonProperty("mail_address")
    private String mailAddress; 
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