package com.daily_app.demo.Dto.Request;

public class UserThemeUpdateRequestDto {
    private String userTheme;

    public UserThemeUpdateRequestDto(String userTheme){
        this.userTheme = userTheme;
    }

    public String getUserTheme() {
        return userTheme;
    }
}
