package com.daily_app.demo.Service;

import com.daily_app.demo.Repository.DailyRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daily_app.demo.Dto.Internal.DailyQueryDto;
import com.daily_app.demo.Dto.Request.ReportRequestDto;
import com.daily_app.demo.Dto.Request.ReportUpdateRequestDto;
import com.daily_app.demo.Dto.Response.ContentDto;
import com.daily_app.demo.Dto.Response.DailyDto;
import com.daily_app.demo.Dto.Response.DailyResponseDto;
import com.daily_app.demo.Dto.Response.WeeklyListResponseDto;
import com.daily_app.demo.Entity.Category;
import com.daily_app.demo.Entity.Daily;
import com.daily_app.demo.Entity.DailyDetail;
import com.daily_app.demo.Entity.User;
import com.daily_app.demo.Entity.WeeklySummary;
import com.daily_app.demo.Repository.CategoryRepository;
import com.daily_app.demo.Repository.DailyDetailRepository;
import com.daily_app.demo.Repository.UserRepository;
import com.daily_app.demo.Repository.WeeklySummaryRepository;

@Service
public class DailyCrudService {

    private final DailyRepository dailyRepository;
    @Autowired
    private WeeklySummaryRepository weeklySummaryRepository;
    @Autowired
    private DailyDetailRepository dailyDetailrepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private WeeklySummaryService weeklySummaryService;

    @Autowired
    private DailySummaryService dailySummaryService;

    DailyCrudService(DailyRepository dailyRepository) {
        this.dailyRepository = dailyRepository;
    }

    // weekly response
    // ====================================================================
    // #region
    @Transactional
    public WeeklyListResponseDto weeklyListResponse(Integer userId) {
        List<WeeklySummary> weeklySummaries = weeklySummaryRepository.findByUserId(userId);
        return WeeklyListResponseDto.EntityToResponseDto(weeklySummaries);
    }
    // #endregion

    // daily reponse=============================================================
    // #region
    @Transactional
    public DailyResponseDto dailyResponse(Integer userId, LocalDate startDate, LocalDate endDate) {
        List<DailyQueryDto> dailiesRawList = dailyDetailrepository.dailiesContentList(
                userId,
                startDate,
                endDate);

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
                        query.getDailyDate());

                dailyDto.setSummary(
                        query.getDailySummaryContent());

                dailyDto.setContents(
                        new ArrayList<>());

                dailyDto.setDailyId(dailyId);

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
        DailyResponseDto responseDto = new DailyResponseDto();

        responseDto.setWeekStartDate(weekStartDate);

        responseDto.setWeekEndDate(weekEndDate);

        responseDto.setDays(
                new ArrayList<>(dailyMap.values()));

        return responseDto;
    }
    // #endregion

    // create daily
    // report=============================================================
    // #region

    @Transactional
    public Map<String, String> reportDaily(ReportRequestDto reportRequest) {
        User user = userRepository.findById(reportRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found: " + reportRequest.getUserId()));
        Integer userId = user.getUserId();
        Daily daily = new Daily(user, reportRequest.getDate());

        List<DailyDetail> details = new ArrayList<DailyDetail>();

        for (com.daily_app.demo.Dto.Request.ContentDto dailyDetailContent : reportRequest.getContents()) {
            Category category = categoryRepository.findById(dailyDetailContent.getCategoryId())
                    .orElseThrow(
                            () -> new RuntimeException("Category not found: " + dailyDetailContent.getCategoryId()));
            details.add(new DailyDetail(daily, category, dailyDetailContent.getContent()));
        }

        daily.getDailyDetails().addAll(details);

        try {
            dailyRepository.save(daily);

        } catch (DataIntegrityViolationException e) {
            return Map.of("status", "failed", "message", "日報の登録に失敗しました");
        }
        weeklySummaryService.createWeeklySummary(userId);
        dailySummaryService.generateSummary(daily, reportRequest.getContents());

        return Map.of("status", "success", "message", "日報の登録に成功しました");
    }
    // #endregion

    // update daily
    // report=============================================================
    // #region
    @Transactional
    public Map<String, String> updateDaily(ReportUpdateRequestDto updateRequest) {
        Daily daily = dailyRepository.findById(updateRequest.getDailyId())
                .orElseThrow(() -> new RuntimeException("Daily not found: " + updateRequest.getDailyId()));
        Integer userId = daily.getUserId().getUserId();
        LocalDate date = daily.getDailyDate();
        List<DailyDetail> details = daily.getDailyDetails();

        for (DailyDetail detail : details) {
            for (com.daily_app.demo.Dto.Request.ContentDto content : updateRequest.getContents()) {
                if (detail.getCategory().getCategoryId() == content.getCategoryId()) {
                    detail.setContent(content.getContent());
                }
            }
        }

        daily.setDailyDetails(details);

        try {
            dailyRepository.save(daily);
        } catch (DataIntegrityViolationException e) {
            return Map.of("status", "failed", "message", "日報の更新に失敗しました");
        }

        dailySummaryService.generateSummary(daily, updateRequest.getContents());
        weeklySummaryService.updateWeeklySummary(userId, date);

        return Map.of("status", "success", "message", "日報の更新に成功しました");
    }
    // #endregion

    // delete daily
    // report=============================================================
    // #region
    @Transactional
    public Map<String, String> deleteDaily(Integer dailyId) {
        Daily daily = dailyRepository.findById(dailyId)
        .orElseThrow(() -> new RuntimeException("Daily not found: " + dailyId));
        Integer userId = daily.getUserId().getUserId();
        LocalDate date = daily.getDailyDate();
        try {
            dailyRepository.deleteById(dailyId);
        } catch (DataIntegrityViolationException e) {
            return Map.of("status", "failed", "message", "日報の削除に失敗しました");
        }
        weeklySummaryService.updateWeeklySummary(userId, date);
        return Map.of("status", "success", "message", "日報の削除に成功しました");
    }
    // #endregion
}
