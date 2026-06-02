package com.daily_app.demo.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="daily_summaries")
public class DailySummary {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_summaries")
    private Integer dailySummaryId;

    @OneToOne
    @JoinColumn(name = "daily_id")
    private Daily daily;

    @Column(name = "daily_summary_conent")
    private String dailySummaryContent;

    //constructer======================================

    public DailySummary(){}

    public DailySummary(Daily daily, String dailySummaryContent){
        this.daily = daily;
        this.dailySummaryContent = dailySummaryContent;

    }
}
