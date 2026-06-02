package com.daily_app.demo.Dto.Response;

import java.time.LocalDate;
import java.util.List;

public class DailyDto {

    private LocalDate date;

    private List<ContentDto> categories;

    public DailyDto() {
    }

    public DailyDto(LocalDate date, List<ContentDto> categories) {
        this.date = date;
        this.categories = categories;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<ContentDto> getCategories() {
        return categories;
    }

    public void setCategories(List<ContentDto> categories) {
        this.categories = categories;
    }
}