package com.daily_app.demo.Dto.Response;

import java.time.LocalDate;
import java.util.List;

public class DailyResponseDto {

    private LocalDate weekStartDate;
    private LocalDate weekEndDate;

    private List<DailyDto> days;

    public DailyResponseDto() {
    }

    public DailyResponseDto(LocalDate weekStartDate,
            LocalDate weekEndDate,
            List<DailyDto> days) {
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

    public List<DailyDto> getDays() {
        return days;
    }

    public void setDays(List<DailyDto> days) {
        this.days = days;
    }
}