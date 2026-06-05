// 松原編集
package com.daily_app.demo.Service;

import org.springframework.stereotype.Component;
import com.daily_app.demo.Entity.User;

// 必要になる予定↓
// import com.daily_app.demo.Entity.DailyReport;
// import com.daily_app.demo.Repository.DailyReportRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

@Component
public class RemindMessageGenerator {

   // 日報の提出状況を確認するために、日報のレポジトリを注入
   // @Autowired
   // private DailyReportRepository dailyReportRepository;

   private static final ZoneId ZONE = ZoneId.of("Asia/Tokyo");

   /**
     * ユーザーの日報提出状況に合わせてリマインドメッセージを生成する
     */
    public String generateMessage(User user) {
        // 1. 昨日の日付を取得
        LocalDate yesterday = LocalDate.now(ZONE).minusDays(1);

        // 2. 昨日の日報をDBから探す
        // ※リポジトリに findByUserAndReportDate(User user, LocalDate date) のようなメソッドがある想定です
        // Optional<DailyReport> yesterdayReportOpt = dailyReportRepository.findByUserAndReportDate(user, yesterday);
        
        // 【テスト用ダミー処理】実際のリポジトリを繋ぐまでは、ここを切り替えてテストしてください
        Optional<String> yesterdayGoalOpt = Optional.empty(); // 日報がない場合
        // Optional<String> yesterdayGoalOpt = Optional.of("JavaのJPAをマスターする！"); // 日報（目標）がある場合

        // 3. 日報があるかどうかでメッセージを分岐
        if (yesterdayGoalOpt.isEmpty()) {
            // 昨日の日報を登録していない場合
            return "昨日の日報がまだ提出されていません。日報を提出しましょう！";
        } else {
            // 昨日の日報を登録している場合、目標を取り出してメッセージに組み込む
            String yesterdayGoal = yesterdayGoalOpt.get();
            return "昨日の目標は「" + yesterdayGoal + "」でした。今日の日報を提出しましょう！";
        }
    }
}

    

