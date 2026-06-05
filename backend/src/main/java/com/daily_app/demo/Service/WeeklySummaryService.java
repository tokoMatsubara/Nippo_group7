package com.daily_app.demo.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.daily_app.demo.Dto.Internal.DailyQueryDto;
import com.daily_app.demo.Dto.Response.ContentDto;
import com.daily_app.demo.Dto.Response.DailyDto;
import com.daily_app.demo.Dto.Response.DailyResponseDto;
import com.daily_app.demo.Entity.WeeklySummary;
import com.daily_app.demo.Repository.DailyDetailRepository;
import com.daily_app.demo.Repository.WeeklySummaryRepository;

@Service
public class WeeklySummaryService {

    private final WeeklySummaryRepository weeklySummaryRepository;

    @Autowired
    private CallLlmService callLlmService;

    @Autowired
    private DailyDetailRepository dailyDetailRepository;

    WeeklySummaryService(WeeklySummaryRepository weeklySummaryRepository) {
        this.weeklySummaryRepository = weeklySummaryRepository;
    }

    // =========================================
    // ① 外から呼ばれるメイン処理
    // =========================================
    @Async
    public void createWeeklySummary(Integer userId) {

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

        // ① DBから生データ取得（raw）
        List<DailyQueryDto> raw = getWeeklyData(userId, startOfWeek, endOfWeek);

        raw.forEach(r -> System.out.println("dailyId=" + r.getDailyId() + " content=" + r.getContent()));

        // ② LLM用に整形
        List<DailyDto> weeklyData = convertToDailyDto(raw);

        // ③ 非同期でLLM実行
        summarizeWeekly(weeklyData)
                .thenAccept(summary -> {
                    System.out.println("週次要約: " + summary);
                    WeeklySummary entity = new WeeklySummary(
                            userId,
                            summary,
                            startOfWeek,
                            endOfWeek);
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

    // =========================================
    // ③ raw → LLM用DTO変換
    // =========================================
    private List<DailyDto> convertToDailyDto(List<DailyQueryDto> rawList) {

        Map<Integer, DailyDto> map = new LinkedHashMap<>();

        for (DailyQueryDto q : rawList) {

            // 初回だけ日単位DTO作成
            map.computeIfAbsent(q.getDailyId(), id -> {
                DailyDto dto = new DailyDto();
                dto.setDate(q.getDailyDate());
                dto.setSummary(q.getDailySummaryContent());
                dto.setContents(new ArrayList<>());
                return dto;
            });

            // 内容追加
            map.get(q.getDailyId())
                    .getContents()
                    .add(new ContentDto(
                            q.getCategoryId(),
                            q.getCategoryName(),
                            q.getContent()));
        }

        return new ArrayList<>(map.values());
    }

    public CompletableFuture<String> summarizeWeekly(List<DailyDto> weeklyData) {

        System.out.println("START THREAD: " + Thread.currentThread().getName());

        String prompt = buildPrompt(weeklyData);

        String result = callLlmService.chatResponse(prompt);

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
                この内容を300文字程度で簡潔に要約してください。

                ### 日報データ
                %s

                ### 出力ルール
                ・日本語
                ・簡潔
                ・箇条書き禁止
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