package com.daily_app.demo.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dailies")
public class Daily {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dailies")
    private Integer dailyId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    //// constructer======================================

    public Daily() {
    }

    public Daily(Integer userId) {
        this.userId = userId;
    }

    // getter======================================

    public Integer getDailyId() {
        return dailyId;
    }

    public Integer getUserId() {
        return userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // setter======================================

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
