package com.daily_app.demo.Dto.Response;

public class RemindIsReadDto {
    boolean isRead;
    String message;

    public RemindIsReadDto(boolean isRead){
        this.isRead = isRead;
        this.message = isRead ? "すべて既読済み" : "未読あり";
    }

    public boolean getIsRead(){
        return isRead;
    }

    public String getMessage() {
        return message;
    }
}
