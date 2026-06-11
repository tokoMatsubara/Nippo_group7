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
}