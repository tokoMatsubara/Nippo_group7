package com.daily_app.demo.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="daily_details")
public class DailyDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_detail_id")
    private Integer dailyDetailId;

    @Column(name = "daily_id")
    private Integer dailyId;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "content")
    private String content;

    //constructer======================================

    public DailyDetail(){}

    public DailyDetail(Integer dailyId, Integer categoryId, String content){
        this.dailyId = dailyId;
        this. categoryId = categoryId;
        this.content = content;
    }

    //getter======================================
    
    public Integer getCategoryId() {
        return categoryId;
    }
    public String getContent() {
        return content;
    }

    //setter======================================

    public void setContent(String content) {
        this.content = content;
    }
}
