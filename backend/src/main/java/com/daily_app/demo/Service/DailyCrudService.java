package com.daily_app.demo.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.daily_app.demo.Dto.Internal.DailiesQueryDto;
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
        return WeeklyListResponseDto.EntityToResponseDto(weeklySummaries);
    }


    //dailyReponse=============================================================

    public DailyResponseDto dailyResponse(Integer userId, LocalDate startDate, LocalDate endDate){
        List<DailiesQueryDto> dailiesRawList = dailyDetailrepository.dailiesContentList(userId, startDate, endDate);
        
        DailyResponseDto dailyResponse = new DailyResponseDto();
        return dailyResponse;
    }

    
}
