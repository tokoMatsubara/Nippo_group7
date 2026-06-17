package com.daily_app.demo.Dto.Request;

public class EmailUpdateRequestDto {
    private String mailAddress;

    public EmailUpdateRequestDto(String mailAddress){
        this.mailAddress = mailAddress;
    }
    
    public String getMailAddress() {
        return mailAddress;
    }
}
