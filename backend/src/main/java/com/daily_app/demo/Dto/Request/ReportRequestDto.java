package com.daily_app.demo.Dto.Request;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ReportRequestDto {

    private Integer userId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private List<ContentDto> contents;

    public static class ContentDto {

        private int categoryId;
        private String content;

        public ContentDto() {
        }

        public ContentDto(int categoryId, String content) {
            this.categoryId = categoryId;
            this.content = content;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

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