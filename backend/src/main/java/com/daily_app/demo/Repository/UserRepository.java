//　松原編集
package com.daily_app.demo.Repository; 

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daily_app.demo.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
}
