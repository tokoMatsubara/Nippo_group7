package com.daily_app.demo.Dto.Response;

public class ContentDto {

    private Integer categoryId;

    private String categoryName; // ← フロント表示用（重要）

    private String content;

    public ContentDto() {
    }

    public ContentDto(Integer categoryId, String categoryName, String content) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.content = content;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}