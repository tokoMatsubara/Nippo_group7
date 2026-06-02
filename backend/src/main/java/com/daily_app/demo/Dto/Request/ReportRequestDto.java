package com.daily_app.demo.Dto.Request;

import java.util.List;

public class ReportRequestDto {

    /**
     * ユーザーのID
     */
    private Long userId;

    /**
     * それぞれのカテゴリ別の日報の内容
     */
    private List<ContentDto> contents;

    public ReportRequestDto() {
    }

    public ReportRequestDto(Long userId, List<ContentDto> contents) {
        this.userId = userId;
        this.contents = contents;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<ContentDto> getContents() {
        return contents;
    }

    public void setContents(List<ContentDto> contents) {
        this.contents = contents;
    }

    public static class ContentDto {

        /**
         * 内容一つのカテゴリ
         */
        private String category;

        /**
         * カテゴリに該当する内容
         */
        private String content;

        public ContentDto() {
        }

        public ContentDto(String category, String content) {
            this.category = category;
            this.content = content;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}