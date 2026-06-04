package com.daily_app.demo.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.daily_app.demo.Dto.Response.DailyDto;

@Service
public class WeeklySummaryService {

    private final CallLlmService callLlmService;

    public WeeklySummaryService(CallLlmService callLlmService) {
        this.callLlmService = callLlmService;
    }

    /**
     * 週次要約生成
     */
    @Async
    public CompletableFuture<String> summarizeWeekly(List<DailyDto> weeklyData) {

        System.out.println("START: " + Thread.currentThread().getName());

        String prompt = buildPrompt(weeklyData);

        System.out.println("==== PROMPT ====");
        System.out.println(prompt);

        String result = callLlmService.chatResponse(prompt);

        System.out.println(result);
        System.out.println("END: " + Thread.currentThread().getName());

        return CompletableFuture.completedFuture(result);
    }

    /**
     * プロンプト作成
     */

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

    /**
     * Daily → text
     */
    private String convertDailyToText(DailyDto daily) {

        String contents = daily.getContents().stream()
                .map(c -> "・[" + c.getCategoryId() + "] " + c.getContent())
                .collect(Collectors.joining("\n"));

        return "日付: " + daily.getDate()
                + "\n要約: " + daily.getSummary()
                + "\n内容:\n" + contents;
    }
}