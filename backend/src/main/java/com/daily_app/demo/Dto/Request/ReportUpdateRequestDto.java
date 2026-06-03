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
}