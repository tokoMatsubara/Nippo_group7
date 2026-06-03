package com.daily_app.demo.Dto.Request;

import java.util.List;

public class ReportUpdateRequestDto {

    private Integer daily_id;

    /**
     * 日報の内容リスト
     */
    private List<ContentDto> contents;

    public ReportUpdateRequestDto() {
    }

    public ReportUpdateRequestDto(Integer daily_id, List<ContentDto> contents) {
        this.daily_id = daily_id;
        this.contents = contents;
    }

    public Integer getDailyId() {
        return daily_id;
    }

    public void setDailyId(Integer daily_id) {
        this.daily_id = daily_id;
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