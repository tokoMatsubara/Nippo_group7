package com.daily_app.demo.Dto;

import java.time.LocalDateTime;

public class DailiesQueryDto {
    private Integer dailyId;
    private LocalDateTime createdAt;
    private Integer categoryId;
    private String categoryName;
    private String content;

    public DailiesQueryDto(){}
    
    public Integer getDailyId() {
        return dailyId;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public Integer getCategoryId() {
        return categoryId;
    }
    public String getCategoryName() {
        return categoryName;
    }
    public String getContent() {
        return content;
    }
}
