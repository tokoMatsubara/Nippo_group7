// 松原編集
package com.daily_app.demo.Service;

import com.daily_app.demo.Dto.Request.ContentDto;
import com.daily_app.demo.Entity.Daily;
import com.daily_app.demo.Entity.DailySummary;
import com.daily_app.demo.Repository.CategoryRepository;
import com.daily_app.demo.Repository.DailySummaryRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DailySummaryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // 1. CallLlmServiceを利用するためにフィールドを定義
    @Autowired
    private CallLlmService callLlmService;

    @Autowired
    private DailySummaryRepository dailySummaryRepository;

    /**
     * ReportRequestDtoを入力として受け取り、LLM用の要約を生成して返すメソッド
     * * @param requestDto 画面から届いた日報データ
     * @return LLMによって生成された要約文字列
     */
    
    @Async
    @Transactional
    public void generateSummary(Daily daily, List<ContentDto> requestDto) {
        
        // 1. もし日報の中身（contents）が空っぽだったら、空の文字を返す
        if (requestDto == null || requestDto.isEmpty()) {
            // return "日報の内容がないため、要約を生成できませんでした。";
            return;
        }

        // 2. 伝票(Dto)の中にある複数の日報内容（content）を1つの文章にガッチャンコする
        // 例：「〇〇の作業をした」「エラーを解決した」 ➔ 「・〇〇の作業をした\n・エラーを解決した」
        String prompt = buildPrompt(requestDto);

        System.out.println("--- LLMに送信するプロンプト案 ---\n" + prompt);

        // 4. 【本物のLLMを呼び出すロジック】
        // 組み立てたプロンプトをCallLlmServiceに渡して結果を受け取る
        String realSummary;
        try{
            realSummary = callLlmService.chatResponse(prompt);
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("LLM呼び出し失敗(日報要約)");
        }

        //新規の日報登録なら新しいdailysummaryを生成、更新ならdailyidから取得
        DailySummary newSummary = dailySummaryRepository
            .findByDaily_DailyId(daily.getDailyId())
            .orElseGet(DailySummary::new);
        
        newSummary.setDaily(daily);
        newSummary.setDailySummaryContent(realSummary);

        try{
            dailySummaryRepository.save(newSummary);
        }catch(DataIntegrityViolationException e){
            System.err.println("==========日報要約のDB登録失敗==========");
            // return "failed";
            return;
        }

        System.err.println("==========日報要約の登録が完了しました==========");

        // return "success";
    }


    @Transactional
    public void deleteSummary(Integer dailyId) throws Exception{
        dailySummaryRepository.deleteByDaily_DailyId(dailyId).orElseThrow(() -> 
            new RuntimeException("dailySummary was not able to be deleted")
        );
    }

    private String buildPrompt(List<ContentDto> requestContent){
        String combinedContent = requestContent.stream()
                .map(c -> "[" + c.getCategoryId() + "] " 
                    + categoryRepository.findById(c.getCategoryId()).get().getCategoryName()
                    + "\n"
                    + c.getContent())
                .collect(Collectors.joining("\n・", "・", ""));

        // 3. LLMに送るためのプロンプト（命令文）を組み立てる
        String prompt = "以下の日報内容を、簡潔に3行程度で要約してください。\n日報の要約内容以外を絶対に出力しないでください。\n絵文字を出力しないでください\n\n"
                      + "[日報内容]\n" 
                      + combinedContent;
        return prompt;
    }
}
