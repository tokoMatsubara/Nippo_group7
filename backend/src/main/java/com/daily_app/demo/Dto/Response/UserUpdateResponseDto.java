package com.daily_app.demo.Dto.Response;

public class UserUpdateResponseDto {
    private boolean success;
    private String message;
    private String content;

    public UserUpdateResponseDto(boolean success, String message){
        this.success = success;
        this.message = message;
        this.content = "empty";
    }

    public UserUpdateResponseDto(boolean success, String message, String content){
        this.success = success;
        this.message = message;
        this.content = content;
    }

    public boolean getSuccess(){
        return success;
    }
    public String getMessage() {
        return message;
    }
    public String getContent() {
        return content;
    }
}
