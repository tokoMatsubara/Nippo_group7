// 松原編集　
package com.daily_app.demo.Entity; 

import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;




@Entity
@Table(name = "users")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;
    
    @Column(name = "user_theme")
    private String userThema = "blue";

    @Column(name = "role")
    private String role = "USER";

    @NotBlank
    @Column(name = "user_name")
    private String userName;

    @NotBlank
    @Column(name = "password")
    private String password;

    @Email
    @NotBlank
    @Column(name = "mail_address")
    private String mailAddress;

    @Column(name = "remind_status")
    private boolean remindStatus;

    @Column(name = "remind_time")
    private LocalTime remindTime;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Daily> dailies;


    //constructer======================================

    public User(){}

    public User(String userName, String password, String mailAddress, boolean remindStatus, LocalTime remindTime) {
        this.userName = userName;
        this.password = password;
        this.mailAddress = mailAddress;
        // 💡 以下の2行を追記し、引数の値がちゃんと代入されるようにしました
        this.remindStatus = remindStatus;
        this.remindTime = remindTime;
    }

    //getter======================================

    public Integer getUserId() {
        return userId;
    }

    public String getUserTheme(){
        return userThema;
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

    public String getRole() {
        return role;
    }

    public boolean getRemindStatus() {
        return remindStatus;
    }

    public LocalTime getRemindTime() {
        return remindTime;
    }

    //setter======================================

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserTheme(String userThema){
        this.userThema = userThema;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setRemindStatus(boolean remindStatus) {
        this.remindStatus = remindStatus;
    }

    public void setRemindTime(LocalTime remindTime) {
        this.remindTime = remindTime;
    }

}
