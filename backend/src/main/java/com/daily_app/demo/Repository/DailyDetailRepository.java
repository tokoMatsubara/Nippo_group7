package com.daily_app.demo.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.daily_app.demo.Dto.DailiesQueryDto;
import com.daily_app.demo.Entity.DailyDetail;

@Repository
public interface DailyDetailRepository extends JpaRepository<DailyDetail, Long> {

    @Query("""
            SELECT 
                d.daily_id,
                d.created_at,
                c.category_id,
                c.category_name,
                dd.content,
            FROM dailies AS d
            JOIN daily_details AS dd
            ON d.daily_id = dd.daily_id
            JOIN categories AS c 
            ON c.category_id = dd.category_id
            JOIN users AS u
            ON u.user_id = d.user_id
            WHERE u.user_id = :userId
            AND d.created_at BETWEEN :startDate AND :endDate
            """)
        List<DailiesQueryDto> dailiesContentList(Integer userId, LocalDate startDate, LocalDate endDate)
}
