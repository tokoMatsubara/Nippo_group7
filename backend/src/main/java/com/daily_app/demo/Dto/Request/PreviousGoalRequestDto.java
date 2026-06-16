package com.daily_app.demo.Dto.Request;

import java.time.LocalDate;

public class PreviousGoalRequestDto {
    private LocalDate targetDate;

    public PreviousGoalRequestDto(LocalDate targetDate){
        this.targetDate = targetDate;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }
    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }
}
