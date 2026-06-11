// 松原編集
package com.daily_app.demo.Dto.Request;

import com.fasterxml.jackson.annotation.JsonProperty;


public class UserCreateRequestDto {

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
    private String password;

    public UserCreateRequestDto() {
    }

    public UserCreateRequestDto(String userName,
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
}