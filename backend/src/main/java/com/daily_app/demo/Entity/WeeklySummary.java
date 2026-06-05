package com.daily_app.demo.Entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "weekly_summaries")
public class WeeklySummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weekly_summary_id")
    private Integer weeklySummaryId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "weekly_summary_content")
    private String weeklySummaryContent;

    @Column(name = "week_start_date")
    private LocalDate weekStartDate;

    @Column(name = "week_end_date")
    private LocalDate weekEndDate;

    public WeeklySummary() {
    }

    public WeeklySummary(Integer userId, String weeklySummaryContent, LocalDate weekStartDate, LocalDate weekEndDate) {
        this.userId = userId;
        this.weeklySummaryContent = weeklySummaryContent;
        this.weekStartDate = weekStartDate;
        this.weekEndDate = weekEndDate;
    }

    public LocalDate getWeekStartDate() {
        return weekStartDate;
    }

    public LocalDate getWeekEndDate() {
        return weekEndDate;
    }

    public String getWeeklySummaryContent() {
        return weeklySummaryContent;
    }

    //週要約更新のために追加
    public void setWeeklySummaryContent(String weeklySummaryContent) {
        this.weeklySummaryContent = weeklySummaryContent;
    }

    public void setWeekEndDate(LocalDate weekEndDate) {
        this.weekEndDate = weekEndDate;
    }
}
