package com.daily_app.demo.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="daily_summaries")
public class DailySummary {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_summary_id")
    private Integer dailySummaryId;

    @Column(name = "daily_id")
    private Integer dailyId;

    @Column(name = "daily_summary_content")
    private String dailySummaryContent;

    public DailySummary(){}

    public DailySummary(Integer dailyId, String dailySummaryContent){
        this.dailyId = dailyId;
        this.dailySummaryContent = dailySummaryContent;
        
    }
}
