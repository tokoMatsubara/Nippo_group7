package com.daily_app.demo.Dto.Response;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.daily_app.demo.Entity.WeeklySummary;

public class WeeklyListResponseDto {

    private List<SummaryDto> summaries;

    public WeeklyListResponseDto() {
    }

    public WeeklyListResponseDto(List<SummaryDto> summaries) {
        this.summaries = summaries;
    }

    public static WeeklyListResponseDto EntityToResponseDto(List<WeeklySummary> summaries){
        return weeklyListResponse(summaries);
    }

    public List<SummaryDto> getSummaries() {
        return summaries;
    }

    public void setSummaries(List<SummaryDto> summaries) {
        this.summaries = summaries;
    }

    private static SummaryDto weeklySummaryEntityToDto(WeeklySummary weeklySummary){
        LocalDate startDate = weeklySummary.getWeekStartDate();
        LocalDate endDate = weeklySummary.getWeekEndDate();
        String content = weeklySummary.getWeeklySummaryContent();

        return new SummaryDto(startDate, endDate, content);
    }
    private static WeeklyListResponseDto weeklyListResponse(List<WeeklySummary> summaries){
        List<SummaryDto> dtos = new ArrayList<SummaryDto>();
        for (WeeklySummary summary : summaries) {
            dtos.add(weeklySummaryEntityToDto(summary));
        }
        return new WeeklyListResponseDto(dtos);
    }

    public static class SummaryDto {

        /**
         * 週の初めの日付
         */
        private LocalDate startDate;

        /**
         * 週の終わりの日付
         */
        private LocalDate endDate;

        /**
         * 週の要約内容
         */
        private String content;

        public SummaryDto() {
        }

        public SummaryDto(LocalDate startDate, LocalDate endDate, String content) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.content = content;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
