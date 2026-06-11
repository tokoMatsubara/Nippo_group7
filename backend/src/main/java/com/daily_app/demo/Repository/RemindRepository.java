// 松原編集
package com.daily_app.demo.Repository;

// import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daily_app.demo.Entity.Remind;
import com.daily_app.demo.Entity.User;

public interface RemindRepository extends JpaRepository<Remind, Integer>{

    // 🌟特注ボタン：「このユーザー」に「この内容」のリマインドがすでに存在するかチェックする
    boolean existsByUserAndRemindContent(User user, String remindContent);

    // テストのためにコメントアウトしているだけで必要です。
    // 「毎日リマインドを送りたい（毎日レコードを作りたい）」のであれば、存在チェックに「今日の日付」も含める必要がある
    //boolean existsByUserAndRemindContentAndCreatedAtAfter(User user, String remindContent, LocalDateTime dateTime);

    List<Remind> findByUser_UserIdOrderByRemindIdDesc(Integer userId);

    List<Remind> findByUser_UserId(Integer userId);
    
    
}
