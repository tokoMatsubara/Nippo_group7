package com.daily_app.demo.Entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "daily", cascade = CascadeType.ALL)
    private List<DailyDetail> dailyDetails;

    //constructer======================================

    public Daily() {
    }

    public Daily(User user){
        this.user = user;
        dailyDetails = new ArrayList<>();
    }

    // getter======================================

    public Integer getDailyId() {
        return dailyId;
    }
    public User getUserId() {
        return user;
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

    // setter======================================

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public void setDailyDetails(List<DailyDetail> dailyDetails) {
        this.dailyDetails = dailyDetails;
    }
}
