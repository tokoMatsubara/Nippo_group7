package com.daily_app.demo.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.Optional;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.daily_app.demo.Dto.Internal.DailyQueryDto;
import com.daily_app.demo.Dto.Response.ContentDto;
import com.daily_app.demo.Dto.Response.DailyDto;
import com.daily_app.demo.Entity.Daily;
import com.daily_app.demo.Entity.DailyDetail;
import com.daily_app.demo.Entity.DailySummary;
import com.daily_app.demo.Entity.WeeklySummary;
import com.daily_app.demo.Repository.DailyDetailRepository;
import com.daily_app.demo.Repository.DailyRepository;
import com.daily_app.demo.Repository.WeeklySummaryRepository;


@Service
public class WeeklySummaryService {

    private final WeeklySummaryRepository weeklySummaryRepository;
    private final CallLlmService callLlmService;
    private final DailyDetailRepository dailyDetailRepository;
    private final DailyRepository dailyRepository;


    WeeklySummaryService(
        WeeklySummaryRepository weeklySummaryRepository,
        DailyRepository dailyRepository,
        DailyDetailRepository dailyDetailRepository,
        CallLlmService callLlmService) {

        this.weeklySummaryRepository = weeklySummaryRepository;
        this.dailyRepository = dailyRepository;
        this.dailyDetailRepository = dailyDetailRepository;
        this.callLlmService = callLlmService;
    }

    public void handleWeeklySummary(Integer userId, LocalDate date) {

        LocalDate startOfWeek = date.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = date.with(DayOfWeek.FRIDAY);
        System.out.println(endOfWeek.toString());

        Optional<WeeklySummary> existing = weeklySummaryRepository.findByUserIdAndWeekStartDate(userId, startOfWeek);

        if (existing.isEmpty()) {
            createWeeklySummary(userId, startOfWeek, endOfWeek);
        }

        updateWeeklySummary(userId, date);
    }

    // =========================================
    // ① 外から呼ばれるメイン処理
    // =========================================
    public void createWeeklySummary(Integer userId, LocalDate startOfWeek, LocalDate endOfWeek) {
        System.out.println("createWeeklySummary START userId=" + userId);

        WeeklySummary entity = new WeeklySummary(
                userId,
                "週要約はまだ作成されていません",
                startOfWeek,
                endOfWeek);

        weeklySummaryRepository.save(entity);
    }

    @Async
    public void updateWeeklySummary(Integer userId, LocalDate targetDate) {
        System.out.println("★★ updateWeeklySummary CALLED ★★ userId=" + userId);

        LocalDate startOfWeek = targetDate.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = targetDate.with(DayOfWeek.FRIDAY);

        // ① 日報データのリスト取得
        List<Daily> dailyList = dailyRepository.findWeeklyWithDetails(userId, startOfWeek, endOfWeek);

        // ② LLM用に整形
        List<DailyDto> weeklyData = toDailyDtoList(dailyList);

        summarizeWeekly(weeklyData)
                .thenAccept(summary -> {

                    Optional<WeeklySummary> existing = weeklySummaryRepository.findByUserIdAndWeekStartDate(
                            userId, startOfWeek);

                    WeeklySummary entity = existing
                            .orElseGet(() -> new WeeklySummary(userId, "", startOfWeek, endOfWeek));

                    entity.setWeeklySummaryContent(summary);
                    entity.setWeekEndDate(endOfWeek);

                    weeklySummaryRepository.save(entity);
                })
                .exceptionally(ex -> {
                    System.out.println("エラー: " + ex.getMessage());
                    return null;
                });
    }

    // =========================================
    // ② DBから取得（raw）
    // =========================================
    public List<DailyQueryDto> getWeeklyData(Integer userId, LocalDate startOfWeek, LocalDate endOfWeek) {

        return dailyDetailRepository.dailiesContentList(
                userId, startOfWeek,
                endOfWeek.plusDays(1));
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

    public CompletableFuture<String> summarizeWeekly(List<DailyDto> weeklyData) {

        System.out.println("START THREAD: " + Thread.currentThread().getName());

        String prompt = buildPrompt(weeklyData);
        String result;

        try {
            result = callLlmService.chatResponse(prompt);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("LLM呼び出し失敗", e);
        }

        System.out.println(prompt);

        System.out.println("END THREAD: " + Thread.currentThread().getName());

        return CompletableFuture.completedFuture(result);
    }

    // =========================================
    // ⑤ プロンプト生成
    // =========================================
    private String buildPrompt(List<DailyDto> dailyList) {

        String input = dailyList.stream()
                .map(this::convertDailyToText)
                .collect(Collectors.joining("\n\n"));

        return """
                以下は1週間の日報です。
                この内容を150文字程度で簡潔に要約してください。

                ### 日報データ
                %s

                ### 出力ルール
                ・日本語
                ・簡潔
                ・箇条書き禁止
                ・総文字数は書かないで
                ・絵文字禁止
                ・要約の内容以外のコミュニケーション出力禁止
                """.formatted(input);
    }

    // =========================================
    // ⑥ DTO → テキスト変換
    // =========================================
    private String convertDailyToText(DailyDto daily) {

        String contents = daily.getContents().stream()
                .map(c -> "・[" + c.getCategoryId() + "] " + c.getContent())
                .collect(Collectors.joining("\n"));

        return "日付: " + daily.getDate()
                + "\n要約: " + daily.getSummary()
                + "\n内容:\n" + contents;
    }
}