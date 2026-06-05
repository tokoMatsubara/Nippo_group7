package com.daily_app.demo.Dto.Request;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ReportRequestDto {

    private Integer userId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private List<ContentDto> contents;

    public ReportRequestDto() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
}