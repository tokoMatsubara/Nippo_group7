//　松原編集
package com.daily_app.demo.Repository; 

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daily_app.demo.Entity.User;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    

    //ログイン処理や重複チェック：「メールアドレスからユーザーを探す」
    Optional<User> findByMailAddress(String mailAddress);
    
}
