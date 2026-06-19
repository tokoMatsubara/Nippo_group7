// 松原編集
package com.daily_app.demo.Dto.Request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class UserInfoRequestDto {

    /**
     * ユーザーの名前
     */
    @JsonProperty("user_name")
    private String userName;

    /**
     * ユーザーのメールアドレス
     */
    @JsonProperty("mail_address")
    private String mailAddress;

    /**
     * ユーザーのパスワード
     */
    @NotBlank
    @Size(min = 8, max = 20, message = "パスワードは8~20文字です")
    private String password;

    /**
     * ユーザーのテーマカラー（追加項目）
     */
    @JsonProperty("user_theme")
    private String userTheme;



    public UserInfoRequestDto() {
    }

    public UserInfoRequestDto(String userName,
                                String mailAddress,
                                String password) {
        this.userName = userName;
        this.mailAddress = mailAddress;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

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

    public String getUserTheme() {
        return userTheme;
    }

    public void setUserTheme(String userTheme) {
        this.userTheme = userTheme;
    } 
}