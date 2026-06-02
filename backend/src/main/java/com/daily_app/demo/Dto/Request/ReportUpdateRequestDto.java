package com.daily_app.demo.Dto.Request;

import java.time.LocalDate;
import java.util.List;

public class ReportUpdateRequestDto {

    /**
     * 対象の日付（変更したい場合のみ使用）
     */
    private LocalDate date;

    /**
     * 日報の内容リスト
     */
    private List<ContentDto> contents;

    public ReportUpdateRequestDto() {
    }

    public ReportUpdateRequestDto(LocalDate date, List<ContentDto> contents) {
        this.date = date;
        this.contents = contents;
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