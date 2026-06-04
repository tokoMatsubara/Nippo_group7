package com.daily_app.demo.Dto.Response;

import java.time.LocalDate;
import java.util.List;

public class DailyDto {

    private Integer dailyId;

    private LocalDate date;

    private List<ContentDto> contents;

    private String summary;

    public DailyDto() {
    }

    public DailyDto(Integer dailyId, LocalDate date, List<ContentDto> contents, String summary) {
        this.dailyId = dailyId;
        this.date = date;
        this.contents = contents;
        this.summary = summary;
    }

    public Integer getDailyId() {
        return dailyId;
    }

    public void setDailyId(Integer dailyId) {
        this.dailyId = dailyId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<ContentDto> getContents() {
        return contents;
    }

    public void setContents(List<ContentDto> contents) {
        this.contents = contents;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}