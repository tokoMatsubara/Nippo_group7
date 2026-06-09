// 松原編集
package com.daily_app.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.daily_app.demo.Entity.User;

import com.daily_app.demo.Entity.Daily;
import com.daily_app.demo.Entity.DailyDetail;
import com.daily_app.demo.Repository.DailyRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.List;

@Component
public class RemindMessageGenerator {

   // 日報の提出状況を確認するために、日報のレポジトリを注入
   @Autowired
   private DailyRepository dailyRepository;

   private static final ZoneId ZONE = ZoneId.of("Asia/Tokyo");

   /**
     * ユーザーの日報提出状況に合わせてリマインドメッセージを生成する
     */
    public String generateMessage(User user) {
        // 1. 前営業日の日付を取得
        //LocalDate previousBusinessDay = BusinessDayUtil.previousBusinessDay(LocalDate.now(ZONE));
        LocalDate previousBusinessDay = BusinessDayUtil.previousBusinessDay(LocalDate.of(2026, 6, 9));
        // 2. 前営業日の日報をDBから探す
        // ※リポジトリに findByUserAndReportDate(User user, LocalDate date)
        Optional<Daily> previousBusinessDayDailyOpt = dailyRepository.findByUserAndDailyDate(user, previousBusinessDay);
        
        // 3. 日報があるかどうかでメッセージを分岐
        if (previousBusinessDayDailyOpt.isEmpty()) {
            // 前営業日の日報を登録していない場合
            return previousBusinessDay + "昨日の日報がまだ提出されていません。日報を提出しましょう！";
        } else {

            // すでに日報を提出している
            Daily previousBusinessDayDaily = previousBusinessDayDailyOpt.get();

            List<DailyDetail> details = previousBusinessDayDaily.getDailyDetails();

            String previousBusinessDayGoal = "未設定"; // 目標がみつからなかったときの初期値

            if (details != null && !details.isEmpty()) {
                // 💡 修正ポイント：Listの中からカテゴリIDが7の明細を探す
                previousBusinessDayGoal = details.stream()
                        .filter(detail -> detail.getCategory() != null && detail.getCategory().getCategoryId() == 7)
                        .map(DailyDetail::getContent)
                        .findFirst()
                        .orElse("未設定"); // カテゴリ7が見つからなかった場合のフォールバック
            }
            // 昨日の日報を登録している場合、目標を取り出してメッセージに組み込む
            return previousBusinessDay + "の目標は「" + previousBusinessDayGoal + "」でした。今日の日報もこの調子で提出しましょう！";
        }
    }
    
}

    

