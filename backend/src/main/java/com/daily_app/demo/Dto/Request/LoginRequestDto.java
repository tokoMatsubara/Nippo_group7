package com.daily_app.demo.Dto.Request;

/**
 * ログインリクエスト用のDTO（Record版）
 */
public record LoginRequestDto(
    String mailAdress, 
    String password
) {

    public String getMailAddress() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMailAddress'");
    }}