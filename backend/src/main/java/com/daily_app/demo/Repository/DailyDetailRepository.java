package com.daily_app.demo.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.daily_app.demo.Dto.Internal.DailyQueryDto;
import com.daily_app.demo.Entity.DailyDetail;

@Repository
public interface DailyDetailRepository extends JpaRepository<DailyDetail, Long> {

    @Query("""
        SELECT new com.daily_app.demo.Dto.Internal.DailyQueryDto(
            d.dailyId,
            d.createdAt,
            ds.dailySummaryContent,
            c.categoryId,
            c.categoryName,
            dd.content
        )
        FROM Daily d
        JOIN d.dailyDetails dd
        JOIN dd.category c
        JOIN DailySummary ds ON ds.daily = d
        WHERE d.user.userId = :userId
        AND d.dailyDate BETWEEN :startDate AND :endDate
        """)
    List<DailyQueryDto> dailiesContentList(
        @Param("userId") Integer userId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}
