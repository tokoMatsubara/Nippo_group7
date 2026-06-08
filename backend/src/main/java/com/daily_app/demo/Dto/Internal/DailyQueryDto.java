package com.daily_app.demo.Dto.Internal;

import java.time.LocalDate;

public class DailyQueryDto {
    private Integer dailyId;
    private LocalDate dailyDate;
    private String dailySummaryContent;
    private Integer categoryId;
    private String categoryName;
    private String content;

    public DailyQueryDto(
        Integer dailyId,
        LocalDate dailyDate,
        String dailySummaryContent,
        Integer categoryId,
        String categoryName,
        String content
    ){
        this.dailyId = dailyId;
        this.dailyDate = dailyDate;
        this.dailySummaryContent = dailySummaryContent;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.content = content;
    }
    
    public Integer getDailyId() {
        return dailyId;
    }
    public LocalDate getDailyDate() {
        return dailyDate;
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
