package com.daily_app.demo.Entity;
import java.time.LocalDateTime;
import java.util.List;

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
@Table(name="dailies")
public class Daily {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dailies")
    private Integer dailyId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "created_at")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "daily_details", cascade = CascadeType.ALL)
    List<DailyDetail> dailyDetails;

    //constructer======================================

    public Daily(){}

    public Daily(User user){
        this.user = user;
    }

    //getter======================================

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

    //setter======================================

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
