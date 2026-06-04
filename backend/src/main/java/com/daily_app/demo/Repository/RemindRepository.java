package com.daily_app.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daily_app.demo.Entity.Remind;

public interface RemindRepository extends JpaRepository<Remind, Integer>{
    
}
