package com.daily_app.demo.Dto.Response;

import java.time.LocalDate;
import java.util.List;

import com.daily_app.demo.Entity.Daily;

public class DailyResponseDto {

    /**
     * 週の始めの日付
     */
    private LocalDate weekStartDate;

    /**
     * 週の終わりの日付
     */
    private LocalDate weekEndDate;

    /**
     * １週間の日報をまとめた配列データ
     */
    private List<Daily> days;

    public DailyResponseDto() {
    }

    public DailyResponseDto(LocalDate weekStartDate,
                            LocalDate weekEndDate,
                            List<Daily> days) {
        this.weekStartDate = weekStartDate;
        this.weekEndDate = weekEndDate;
        this.days = days;
    }

    public LocalDate getWeekStartDate() {
        return weekStartDate;
    }

    public void setWeekStartDate(LocalDate weekStartDate) {
        this.weekStartDate = weekStartDate;
    }

    public LocalDate getWeekEndDate() {
        return weekEndDate;
    }

    public void setWeekEndDate(LocalDate weekEndDate) {
        this.weekEndDate = weekEndDate;
    }

    public List<Daily> getDays() {
        return days;
    }

    public void setDays(List<Daily> days) {
        this.days = days;
    }
}