package com.daily_app.demo.Dto.Request;

public class ContentDto {

    private Integer categoryId;
    private String content;

    public ContentDto() {
    }

    public ContentDto(Integer categoryId, String content) {
        this.categoryId = categoryId;
        this.content = content;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
