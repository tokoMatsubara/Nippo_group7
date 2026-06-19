package com.daily_app.demo.Dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PasswordUpdateReqeustDto {
    @NotBlank
    @Size(min = 8, max = 20, message = "パスワードは8~20文字です")
    private String currentPassword;
    @NotBlank
    @Size(min = 8, max = 20, message = "パスワードは8~20文字です")
    private String newPassword;

    public PasswordUpdateReqeustDto(String currentPassword, String newPassword){
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }
    public String getNewPassword() {
        return newPassword;
    }
}
