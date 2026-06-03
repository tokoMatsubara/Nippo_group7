package com.daily_app.demo.Service;

import com.daily_app.demo.Dto.Request.ReportRequestDto;
import com.daily_app.demo.Entity.Daily;
import com.daily_app.demo.Entity.DailySummary;
import com.daily_app.demo.Repository.DailyRepository;
import com.daily_app.demo.Repository.DailySummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class DailyCrudService {

    @Autowired
    private DailyRepository dailyRepository;

    @Autowired
    private DailySummaryRepository dailySummaryRepository;

    @Autowired
    private DailySummaryService dailySummaryService;

    /**
     * 日報登録と、同時に要約を生成して保存する
     */
    public void createReportWithSummary(ReportRequestDto requestDto) {
        System.out.println("--- DailyCrudService: 日報登録＆要約生成 業務開始 ---");

        // 1. 引数付きのコンストラクタを使って日報本体を組み立て
        Daily newDaily = new Daily(requestDto.getUserId());
        
        // 2. 作成日時と更新日時の両方に、現在の時間をセット（これで非NULL制約エラーを回避！）
        LocalDateTime now = LocalDateTime.now();
        newDaily.setCreatedAt(now);
        newDaily.setUpdatedAt(now);

        // 3. 本体のリポジトリで保存して、DBが自動採番した本当のID（dailyId）を取得
        Daily savedDaily = dailyRepository.save(newDaily);
        Integer realDailyId = savedDaily.getDailyId(); 
        System.out.println("日報本体を保存しました（本物のID: " + realDailyId + "）");


        // --- 要約の自動生成 ---
        String generatedSummaryText = dailySummaryService.generateSummary(requestDto);


        // --- 要約の保存 ---
        // 4. 採番されたばかりの本物のID（realDailyId）を渡して外部キー制約を突破！
        DailySummary newSummary = new DailySummary(realDailyId, generatedSummaryText);
        dailySummaryRepository.save(newSummary);
        
        System.out.println("データベースに要約を保存しました！");
    }
}