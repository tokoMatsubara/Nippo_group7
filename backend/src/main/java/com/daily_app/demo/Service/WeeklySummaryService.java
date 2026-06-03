package com.daily_app.demo.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.daily_app.demo.Dto.Response.DailyDto;

@Service
public class WeeklySummaryService {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String LM_STUDIO_URL = "http://localhost:1234/v1/chat/completions";

    /**
     * 週次要約生成（LM Studio）
     */
    public String generateWeeklySummary(List<DailyDto> dailyList) {

        String prompt = buildPrompt(dailyList);

        return callLMStudio(prompt);
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

    public String summarizeWeekly(List<DailyDto> weeklyData) {

        String prompt = buildPrompt(weeklyData);

        System.out.println("==== PROMPT ====");
        System.out.println(prompt);

        return callLMStudio(prompt);
    }

    /**
     * LM Studio API呼び出し
     */
    private String callLMStudio(String prompt) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = Map.of(
                "model", "local-model",
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)),
                "temperature", 0.7);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            Map response = restTemplate.postForObject(
                    LM_STUDIO_URL,
                    request,
                    Map.class);

            // 返答取り出し
            List choices = (List) response.get("choices");
            Map message = (Map) ((Map) choices.get(0)).get("message");

            return (String) message.get("content");

        } catch (Exception e) {
            e.printStackTrace();
            return "LLM呼び出し失敗（LM Studio未起動の可能性）";
        }
    }
}