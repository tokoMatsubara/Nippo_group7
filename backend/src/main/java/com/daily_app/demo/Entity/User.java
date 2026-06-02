// 松原編集
package com.daily_app.demo.Entity; 

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "mail_address")
    private String mailAddress;

    @GeneratedValue(strategy = GenerationType.IDENTITY)// 💡 @GeneratedValue を削除していいかも
    @Column(name = "remind_status")
    private boolean remindStatus;

    @GeneratedValue(strategy = GenerationType.IDENTITY)// 💡 @GeneratedValue を削除していいかも
    @Column(name = "remind_time")
    private LocalTime remindTime;

    public User(){}

    public User(String userName, String password, String mailAddress, boolean remindStatus, LocalTime remindTime){
        this.userName = userName;
        this.password = password;
        this.mailAddress = mailAddress;
        // 💡 以下の2行を追記し、引数の値がちゃんと代入されるようにしました
        this.remindStatus = remindStatus;
        this.remindTime = remindTime;
    }

    public Integer getUserId() {
        return userId;
    }
    public String getUserName() {
        return userName;
    }
    public String getPassword() {
        return password;
    }
    public String getMailAddress() {
        return mailAddress;
    }
    public boolean getRemindStatus(){
        return remindStatus;
    }
    public LocalTime getRemindTime() {
        return remindTime;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }
    public void setRemindStatus(boolean remindStatus) {
        this.remindStatus = remindStatus;
    }
    public void setRemindTime(LocalTime remindTime) {
        this.remindTime = remindTime;
    }
    
}
