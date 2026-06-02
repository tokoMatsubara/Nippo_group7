package com.daily_app.demo.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.daily_app.demo.Dto.Response.DailyResponseDto;
import com.daily_app.demo.Dto.Response.WeeklyListResponseDto;
import com.daily_app.demo.Dto.Response.WeeklyListResponseDto.SummaryDto;
import com.daily_app.demo.Entity.WeeklySummary;
import com.daily_app.demo.Repository.DailyDetailRepository;
import com.daily_app.demo.Repository.WeeklySummaryRepository;

public class DailyCrudService {

    @Autowired
    private WeeklySummaryRepository weeklySummaryRepository;
    @Autowired
    private DailyDetailRepository dailyDetailrepository;

    //weeklyResponse ====================================================================

    public WeeklyListResponseDto weeklyListResponse(Integer userId){
        List<WeeklySummary> weeklySummaries = weeklySummaryRepository.findByUserId(userId);
        return weeklyListResponse(weeklySummaries);
    }

    private SummaryDto weeklySummaryEntityToDto(WeeklySummary weeklySummary){
        LocalDate startDate = weeklySummary.getWeekStartDate();
        LocalDate endDate = weeklySummary.getWeekEndDate();
        String content = weeklySummary.getWeeklySummaryContent();

        return new SummaryDto(startDate, endDate, content);
    }
    private WeeklyListResponseDto weeklyListResponse(List<WeeklySummary> summaries){
        List<SummaryDto> dtos = new ArrayList<SummaryDto>();
        for (WeeklySummary summary : summaries) {
            dtos.add(weeklySummaryEntityToDto(summary));
        }
        return new WeeklyListResponseDto(dtos);
    }

    //dailyReponse=============================================================

    public DailyResponseDto dailyResponse(Integer userId, LocalDate startDate, LocalDate endDate){
        
    }

    
}
