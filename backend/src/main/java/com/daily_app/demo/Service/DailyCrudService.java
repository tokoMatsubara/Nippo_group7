package com.daily_app.demo.Service;

import com.daily_app.demo.Repository.DailyRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daily_app.demo.Dto.Request.ReportRequestDto;
import com.daily_app.demo.Dto.Request.ReportUpdateRequestDto;
import com.daily_app.demo.Dto.Response.ContentDto;
import com.daily_app.demo.Dto.Response.DailyDto;
import com.daily_app.demo.Dto.Response.DailyResponseDto;
import com.daily_app.demo.Dto.Response.WeeklyListResponseDto;
import com.daily_app.demo.Entity.Category;
import com.daily_app.demo.Entity.Daily;
import com.daily_app.demo.Entity.DailyDetail;
import com.daily_app.demo.Entity.DailySummary;
import com.daily_app.demo.Entity.User;
import com.daily_app.demo.Entity.WeeklySummary;
import com.daily_app.demo.Event.WeeklySummaryEvent;
import com.daily_app.demo.Repository.CategoryRepository;
import com.daily_app.demo.Repository.UserRepository;
import com.daily_app.demo.Repository.WeeklySummaryRepository;

@Service
public class DailyCrudService {

    @Autowired
    private DailyRepository dailyRepository;
    @Autowired
    private WeeklySummaryRepository weeklySummaryRepository;
    // @Autowired
    // private DailyDetailRepository dailyDetailRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private WeeklySummaryService weeklySummaryService;

    @Autowired
    private DailySummaryService dailySummaryService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

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
    // @Transactional
    public DailyResponseDto dailyResponse(Integer userId, LocalDate startDate, LocalDate endDate) {
        List<Daily> dailies = dailyRepository.findByUser_UserIdAndDailyDateBetween(userId, startDate, endDate);

        return dailiesToResponseDto(dailies, startDate, endDate);
    }

    private DailyResponseDto dailiesToResponseDto(
            List<Daily> dailyList,
            LocalDate weekStartDate,
            LocalDate weekEndDate) {

        // ResponseDto生成
        DailyResponseDto responseDto = new DailyResponseDto();

        responseDto.setWeekStartDate(weekStartDate);
        responseDto.setWeekEndDate(weekEndDate);
        responseDto.setDays(toDailyDtoList(dailyList));

        return responseDto;
    }

    public List<DailyDto> toDailyDtoList(List<Daily> dailyList) {
        List<DailyDto> dailyDtoList = new ArrayList<DailyDto>();

        for (Daily daily : dailyList) {

            List<DailyDetail> dailyDetails = daily.getDailyDetails();

            List<ContentDto> contentList = new ArrayList<ContentDto>();
            for (DailyDetail detail : dailyDetails) {
                contentList.add(detail.toContentDto());
            }

            DailySummary summary = daily.getDailySummary();
            String summaryContent = summary == null ? "要約がまだ生成されていません" : summary.getDailySummaryContent();

            DailyDto dailyDto = new DailyDto(
                    daily.getDailyId(), daily.getDailyDate(), contentList, summaryContent);

            dailyDtoList.add(dailyDto);
        }
        return dailyDtoList;
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

        Optional<WeeklySummary> weeklySummary = weeklySummaryRepository.findByUserIdAndWeekStartDate(userId,
                reportRequest.getDate());

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
            dailySummaryService.generateSummary(daily, reportRequest.getContents());
            weeklySummaryService.createWeeklySummary(userId);  
        } catch (DataIntegrityViolationException e) {
            System.err.println(e.getMessage());
            return Map.of("status", "failed", "message", "日報の登録に失敗しました");
        }
        dailySummaryService.generateSummary(daily, reportRequest.getContents());
        eventPublisher.publishEvent(
                new WeeklySummaryEvent(userId, reportRequest.getDate()));

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
            System.err.println(e.getMessage());
            return Map.of("status", "failed", "message", "日報の更新に失敗しました");
        }

        dailySummaryService.generateSummary(daily, updateRequest.getContents());
        eventPublisher.publishEvent(
                new WeeklySummaryEvent(userId, date));
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
        }catch(Exception e){
            System.err.println(e.getMessage());
            return Map.of("status", "failed", "message", "日報の削除に失敗しました");
        }
        eventPublisher.publishEvent(
                new WeeklySummaryEvent(userId, date));
        return Map.of("status", "success", "message", "日報の削除に成功しました");
    }
    // #endregion
}