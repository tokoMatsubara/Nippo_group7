package com.daily_app.demo.Event.Listener;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.daily_app.demo.Event.WeeklySummaryEvent;
import com.daily_app.demo.Service.WeeklySummaryService;

@Component
public class WeeklySummaryEventListener {

    private final WeeklySummaryService weeklySummaryService;

    public WeeklySummaryEventListener(WeeklySummaryService weeklySummaryService) {
        this.weeklySummaryService = weeklySummaryService;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleWeeklySummaryEvent(WeeklySummaryEvent event) {

        System.out.println("★★ EVENT RECEIVED ★★");

        weeklySummaryService.handleWeeklySummary(
                event.userId(),
                event.targetDate()
        );
    }
}