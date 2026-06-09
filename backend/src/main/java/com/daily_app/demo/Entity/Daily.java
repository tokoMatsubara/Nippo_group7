package com.daily_app.demo.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.daily_app.demo.Dto.Response.ContentDto;
import com.daily_app.demo.Dto.Response.DailyDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "dailies")
public class Daily {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_id")
    private Integer dailyId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "daily_date")
    private LocalDate dailyDate;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "daily", cascade = CascadeType.ALL)
    private List<DailyDetail> dailyDetails;

    @OneToOne(mappedBy = "daily", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private DailySummary dailySummary;

    //constructer======================================

    public Daily() {
    }

    public Daily(User user, LocalDate dailyDate){
        this.user = user;
        this.dailyDate = dailyDate;
        dailyDetails = new ArrayList<>();
    }

    // getter======================================

    public Integer getDailyId() {
        return dailyId;
    }
    public User getUser() {
        return user;
    }

    public LocalDate getDailyDate(){
        return dailyDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public List<DailyDetail> getDailyDetails() {
        return dailyDetails;
    }
    public DailySummary getDailySummary() {
        return dailySummary;
    }

    // setter======================================

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public void setDailyDate(LocalDate dailyDate){
        this.dailyDate = dailyDate;
    }
    public void setDailyDetails(List<DailyDetail> dailyDetails) {
        this.dailyDetails = dailyDetails;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public void setDailySummary(DailySummary dailySummary) {
        this.dailySummary = dailySummary;
    }
}
