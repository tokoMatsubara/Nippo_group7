package com.daily_app.demo.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.daily_app.demo.Dto.Response.WeeklyListResponseDto;
import com.daily_app.demo.Service.DailyCrudService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class DailyController {
    DailyCrudService dailyCrud;

    @GetMapping("/weekly_list")
    public WeeklyListResponseDto getMethodName(@RequestParam String param) {
        return dailyCrud.weeklyListResponse(1);
    }
    
}
