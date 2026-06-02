package com.daily_app.demo.Dto.Request;

public class ContentDto {

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
