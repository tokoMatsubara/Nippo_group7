package com.daily_app.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daily_app.demo.Entity.DailySummary;

@Repository
public interface DailySummaryRepository extends JpaRepository<DailySummary, Long> {
    
}
