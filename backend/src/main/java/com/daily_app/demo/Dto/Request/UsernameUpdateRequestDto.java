package com.daily_app.demo.Dto.Request;

public class UsernameUpdateRequestDto {
    private String userName;
    public UsernameUpdateRequestDto(String userName){
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
