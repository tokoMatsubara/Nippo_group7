package com.daily_app.demo.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daily_app.demo.Entity.WeeklySummary;

@Repository
public interface WeeklySummaryRepository extends JpaRepository<WeeklySummary, Long> {

    List<WeeklySummary> findByUserIdOrderByWeekStartDateDesc(Integer id);

    Optional<WeeklySummary> findByUserIdAndWeekStartDate(
            Integer userId,
            LocalDate weekStartDate);
}
