package com.daily_app.demo.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="daily_details")
public class DailyDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_detail_id")
    private Integer dailyDetailId;

    @ManyToOne
    @JoinColumn(name = "daily_id")
    private Daily daily;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "content")
    private String content;

    //constructer======================================

    public DailyDetail(){}

    public DailyDetail(Daily daily, Category category, String content){
        this.daily = daily;
        this.category = category;
        this.content = content;
    }

    //getter======================================

    public Daily getDaily(){
        return daily;
    }
    public Category getCategory() {
        return category;
    }
    public String getContent() {
        return content;
    }

    //setter======================================

    public void setContent(String content) {
        this.content = content;
    }
}
