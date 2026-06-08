package com.daily_app.demo.Event;

import java.time.LocalDate;

public record WeeklySummaryEvent(
        Integer userId,
        LocalDate targetDate
) {}