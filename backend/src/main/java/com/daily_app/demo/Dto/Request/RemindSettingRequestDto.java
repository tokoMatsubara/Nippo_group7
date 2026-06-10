package com.daily_app.demo.Dto.Request;

import java.time.LocalTime;

public class RemindSettingRequestDto {
    private boolean remindStatus;
    private LocalTime remindTime;

    public RemindSettingRequestDto(boolean remindStatus, LocalTime remindTime){
        this.remindStatus = remindStatus;
        this.remindTime = remindTime;
    }

    public boolean getRemindStatus(){
        return remindStatus;
    }
    public LocalTime getRemindTime() {
        return remindTime;
    }
}
