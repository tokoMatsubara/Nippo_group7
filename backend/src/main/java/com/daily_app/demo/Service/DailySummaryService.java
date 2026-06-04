// 松原編集
package com.daily_app.demo.Service;

import com.daily_app.demo.Dto.Request.ReportRequestDto;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
public class DailySummaryService {

    /**
     * ReportRequestDtoを入力として受け取り、LLM用の要約を生成して返すメソッド
     * * @param requestDto 画面から届いた日報データ
     * @return LLMによって生成された要約文字列
     */
    public String generateSummary(ReportRequestDto requestDto) {
        
        // 1. もし日報の中身（contents）が空っぽだったら、空の文字を返す
        if (requestDto.getContents() == null || requestDto.getContents().isEmpty()) {
            return "日報の内容がないため、要約を生成できませんでした。";
        }

        // 2. 伝票(Dto)の中にある複数の日報内容（content）を1つの文章にガッチャンコする
        // 例：「〇〇の作業をした」「エラーを解決した」 ➔ 「・〇〇の作業をした\n・エラーを解決した」
        String combinedContent = requestDto.getContents().stream()
                .map(com.daily_app.demo.Dto.Request.ContentDto::getContent)
                .collect(Collectors.joining("\n・", "・", ""));

        // 3. LLMに送るためのプロンプト（命令文）を組み立てる
        String prompt = "以下の日報内容を、簡潔に3行程度で要約してください。\n\n"
                      + "[日報内容]\n" 
                      + combinedContent;

        System.out.println("--- LLMに送信するプロンプト案 ---\n" + prompt);

        // 4. 【ここにLLMを呼び出すロジックが入る】
        // 現時点では、LLMの代わりに「モックの要約文章」を返すようにしておきます
        String mockSummary = "【AI要約モック】本日行った作業は以下の通りです。\n"
                           + "複数のタスクが結合され、LLMへ送信する準備が整いました。";

        return mockSummary;
    }
}
