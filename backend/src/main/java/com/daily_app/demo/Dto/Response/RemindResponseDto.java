// 松原編集
package com.daily_app.demo.Dto.Response;

public class RemindResponseDto {
    
    private Integer userId;
    private String remindContent;

    // コンストラクタ
    public RemindResponseDto(Integer userId, String remindContent) {
        this.userId = userId;
        this.remindContent = remindContent;
    }

    // ゲッター（SpringがJSONに変換する時に必要になります）
    public int getUserId() {
        return userId;
    }

    public String getRemindContent() {
        return remindContent;
    }
}