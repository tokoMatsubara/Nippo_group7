package com.daily_app.demo.Dto.Request;

import java.util.List;

public class ReportUpdateRequestDto {

    private Integer dailyId;

    /**
     * 日報の内容リスト
     */
    private List<ContentDto> contents;

    public ReportUpdateRequestDto() {
    }

    public ReportUpdateRequestDto(Integer dailyId, List<ContentDto> contents) {
        this.dailyId = dailyId;
        this.contents = contents;
    }

    public Integer getDailyId() {
        return dailyId;
    }

    public void setDailyId(Integer dailyId) {
        this.dailyId = dailyId;
    }

    public List<ContentDto> getContents() {
        return contents;
    }

    public void setContents(List<ContentDto> contents) {
        this.contents = contents;
    }

    /**
     * 日報のカテゴリ別内容
     */
    public static class ContentDto {

        /**
         * カテゴリID（例：1=学び、2=課題など）
         */
        private int categoryId;

        /**
         * そのカテゴリの内容
         */
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
}