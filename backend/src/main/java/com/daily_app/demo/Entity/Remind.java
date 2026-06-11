// 松原編集
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
@Table(name = "reminds")
public class Remind {
    @Id
    @Column(name = "remind_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer remindId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "remind_content")
    private String remindContent;

    @Column(name = "is_read", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isRead;

    public Remind(){}

    public Remind(User user, String remindContent){
        this.user = user;
        this.remindContent = remindContent;
    }

    public Integer getRemindId() {
        return remindId;
    }
    public User getUser() {
        return user;
    }
    public String getRemindContent() {
        return remindContent;
    }
    public boolean getIsRead(){
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
}
