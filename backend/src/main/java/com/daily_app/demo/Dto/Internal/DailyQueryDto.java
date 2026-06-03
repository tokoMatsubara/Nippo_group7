package com.daily_app.demo.Dto.Internal;

import java.time.LocalDateTime;

public class DailyQueryDto {
    private Integer dailyId;
    private LocalDateTime createdAt;
    private String dailySummaryContent;
    private Integer categoryId;
    private String categoryName;
    private String content;

    public DailyQueryDto(){}
    
    public Integer getDailyId() {
        return dailyId;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public String getDailySummaryContent() {
        return dailySummaryContent;
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
