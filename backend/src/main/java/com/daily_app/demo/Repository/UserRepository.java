//　松原編集
package com.daily_app.demo.Repository; 

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daily_app.demo.Entity.User;

import java.time.LocalTime;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    

    //ログイン処理や重複チェック：「メールアドレスからユーザーを探す」
    Optional<User> findByMailAddress(String mailAddress);
    

    // 🌟この1行を付け足す！：「指定した時間帯（start〜end）にリマインド設定しているユーザーを検索する」
    List<User> findByRemindTimeBetween(LocalTime start, LocalTime end);
    
}
