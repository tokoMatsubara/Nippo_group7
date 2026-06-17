package com.daily_app.demo.Service;

import com.daily_app.demo.Repository.DailyRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daily_app.demo.Dto.Request.ReportRequestDto;
import com.daily_app.demo.Dto.Request.ReportUpdateRequestDto;
import com.daily_app.demo.Dto.Response.ContentDto;
import com.daily_app.demo.Dto.Response.DailyDto;
import com.daily_app.demo.Dto.Response.DailyResponseDto;
import com.daily_app.demo.Dto.Response.PreviousGoalResponseDto;
import com.daily_app.demo.Dto.Response.WeeklyListResponseDto;
import com.daily_app.demo.Entity.Category;
import com.daily_app.demo.Entity.Daily;
import com.daily_app.demo.Entity.DailyDetail;
import com.daily_app.demo.Entity.DailySummary;
import com.daily_app.demo.Entity.User;
import com.daily_app.demo.Entity.WeeklySummary;
import com.daily_app.demo.Event.WeeklySummaryEvent;
import com.daily_app.demo.Repository.CategoryRepository;
import com.daily_app.demo.Repository.WeeklySummaryRepository;

@Service
public class DailyCrudService {

    private final DailyRepository dailyRepository;
    private final WeeklySummaryRepository weeklySummaryRepository;
    private final CategoryRepository categoryRepository;
    private final DailySummaryService dailySummaryService;
    private final ApplicationEventPublisher eventPublisher;

    public DailyCrudService(
            DailyRepository dailyRepository,
            WeeklySummaryRepository weeklySummaryRepository,
            CategoryRepository categoryRepository,
            DailySummaryService dailySummaryService,
            ApplicationEventPublisher eventPublisher) {

        this.dailyRepository = dailyRepository;
        this.weeklySummaryRepository = weeklySummaryRepository;
        this.categoryRepository = categoryRepository;
        this.dailySummaryService = dailySummaryService;
        this.eventPublisher = eventPublisher;
    }

    // weekly response
    // ====================================================================
    // #region
    @Transactional
    public WeeklyListResponseDto weeklyListResponse(Integer userId) {
        List<WeeklySummary> weeklySummaries = weeklySummaryRepository.findByUserIdOrderByWeekStartDateDesc(userId);
        return WeeklyListResponseDto.EntityToResponseDto(weeklySummaries);
    }
    // #endregion

    // daily response=============================================================
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
            System.out.println("dailyId=" + daily.getDailyId());
            System.out.println("detailCount" + dailyDetails.size());

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
    public ResponseEntity<Map<String, String>> reportDaily(User user, ReportRequestDto reportRequest)
            throws DataIntegrityViolationException {
        if (dailyRepository.existsByUserAndDailyDate(user, reportRequest.getDate())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", "failed", "message", "この日付の日報は既に登録されています"));
        }

        LocalDate requestDay = reportRequest.getDate();
        LocalDate today = LocalDate.now();
        if (requestDay.isAfter(today)) {
            throw new DataIntegrityViolationException("未来の日報を記述しようとしています");
        }

        DayOfWeek requestDow = requestDay.getDayOfWeek();
        if (requestDow == DayOfWeek.SATURDAY || requestDow == DayOfWeek.SUNDAY) {
            throw new DataIntegrityViolationException("休日の日報を記述しようとしています");
        }

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
        System.out.println("details size = " + details.size());

        // DBに登録処理
        dailyRepository.save(daily);

        dailySummaryService.generateSummary(daily, reportRequest.getContents());
        eventPublisher.publishEvent(
                new WeeklySummaryEvent(userId, reportRequest.getDate()));

        return ResponseEntity.ok(Map.of("status", "success", "message", "日報の登録に成功しました"));
    }
    // #endregion

    // update daily
    // report=============================================================
    // #region
    @Transactional
    public Map<String, String> updateDaily(User user, ReportUpdateRequestDto updateRequest)
            throws DataIntegrityViolationException {
        Daily daily = dailyRepository.findById(updateRequest.getDailyId())
                .orElseThrow(() -> new RuntimeException("Daily not found: " + updateRequest.getDailyId()));
        Integer userId = user.getUserId();
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

        if (!daily.getUser().getUserId().equals(userId)) {
            return Map.of("status", "failed", "message", "違うユーザーの日報を更新しようとしています");
        }

        dailyRepository.save(daily);

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
    public Map<String, String> deleteDaily(User user, Integer dailyId) throws DataIntegrityViolationException {
        Daily daily = dailyRepository.findById(dailyId)
                .orElseThrow(() -> new RuntimeException("Daily not found: " + dailyId));
        Integer userId = user.getUserId();
        LocalDate date = daily.getDailyDate();
        if (!daily.getUser().getUserId().equals(userId)) {
            return Map.of("status", "failed", "message", "違うユーザーの日報を削除しようとしています");
        }

        dailyRepository.deleteById(dailyId);

        eventPublisher.publishEvent(
                new WeeklySummaryEvent(userId, date));
        return Map.of("status", "success", "message", "日報の削除に成功しました");
    }
    // #endregion

    @Transactional(readOnly = true)
    public PreviousGoalResponseDto previousGoal(User user, LocalDate targetDate) {

        LocalDate previousDate = BusinessDayUtil.previousBusinessDay(
                targetDate);

        System.out.println("前営業日 = " + previousDate);

        Daily daily = dailyRepository
                .findByUserAndDailyDate(
                        user,
                        previousDate)
                .orElse(null);

        if (daily == null) {

            return new PreviousGoalResponseDto(
                    "前日の日報はありません");
        }

        String goal = daily.getDailyDetails()
                .stream()
                .filter(detail -> detail.getCategory()
                        .getCategoryId() == 7)
                .map(DailyDetail::getContent)
                .findFirst()
                .orElse("前日の日報はありません");

        return new PreviousGoalResponseDto(goal);
    }

    @Transactional(readOnly = true)
    public PreviousGoalResponseDto previousGoal1(User user) {

        LocalDate previousDate = BusinessDayUtil.previousBusinessDay(
                LocalDate.now());

        System.out.println("前営業日 = " + previousDate);

        Daily daily = dailyRepository
                .findByUserAndDailyDate(
                        user,
                        previousDate)
                .orElse(null);

        if (daily == null) {

            return new PreviousGoalResponseDto(
                    "前日の日報はありません");
        }

        String goal = daily.getDailyDetails()
                .stream()
                .filter(detail -> detail.getCategory()
                        .getCategoryId() == 7)
                .map(DailyDetail::getContent)
                .findFirst()
                .orElse("前日の日報はありません");

        return new PreviousGoalResponseDto(goal);
    }
}