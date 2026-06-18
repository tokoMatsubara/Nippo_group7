package com.daily_app.demo.Dto.Request;

public class PasswordUpdateReqeustDto {
    private String currentPassword;
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
