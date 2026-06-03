package com.daily_app.demo.Dto.Response;

import java.time.LocalDate;
import java.util.List;

public class DailyDto {

    private LocalDate date;

    private List<ContentDto> contents;

    private String summary;

    public DailyDto() {
    }

    public DailyDto(LocalDate date, List<ContentDto> contents, String summary) {
        this.date = date;
        this.contents = contents;
        this.summary = summary;
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