package com.daily_app.demo.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daily_app.demo.Dto.Internal.DailyQueryDto;
import com.daily_app.demo.Dto.Response.ContentDto;
import com.daily_app.demo.Dto.Response.DailyDto;
import com.daily_app.demo.Dto.Response.DailyResponseDto;
import com.daily_app.demo.Dto.Response.WeeklyListResponseDto;
import com.daily_app.demo.Dto.Response.WeeklyListResponseDto.SummaryDto;
import com.daily_app.demo.Entity.WeeklySummary;
import com.daily_app.demo.Repository.DailyDetailRepository;
import com.daily_app.demo.Repository.WeeklySummaryRepository;

@Service
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
        List<DailyQueryDto> dailiesRawList = dailyDetailrepository.dailiesContentList(
                userId, startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay()
        );

        return QueryDtoToResponseDto(dailiesRawList, startDate, endDate);
    }

    private DailyResponseDto QueryDtoToResponseDto(
            List<DailyQueryDto> queryList,
            LocalDate weekStartDate,
            LocalDate weekEndDate) {

        Map<Integer, DailyDto> dailyMap = new LinkedHashMap<>();

        for (DailyQueryDto query : queryList) {

            Integer dailyId = query.getDailyId();

            // DailyDto未作成なら生成
            if (!dailyMap.containsKey(dailyId)) {

                DailyDto dailyDto = new DailyDto();

                dailyDto.setDate(
                        query.getCreatedAt().toLocalDate());

                dailyDto.setSummary(
                        query.getDailySummaryContent());

                dailyDto.setContents(
                        new ArrayList<>());

                dailyMap.put(dailyId, dailyDto);
            }

            // ContentDto生成
            ContentDto contentDto = new ContentDto(
                    query.getCategoryId(),
                    query.getCategoryName(),
                    query.getContent());


            // DailyDtoへ追加
            dailyMap.get(dailyId)
                    .getContents()
                    .add(contentDto);
        }

        // ResponseDto生成
        DailyResponseDto responseDto =
                new DailyResponseDto();

        responseDto.setWeekStartDate(weekStartDate);

        responseDto.setWeekEndDate(weekEndDate);

        responseDto.setDays(
                new ArrayList<>(dailyMap.values()));

        return responseDto;
    }

    
}
