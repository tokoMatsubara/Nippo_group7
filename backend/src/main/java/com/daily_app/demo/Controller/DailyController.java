package com.daily_app.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.daily_app.demo.Dto.Request.ContentDto;
import com.daily_app.demo.Dto.Request.ReportRequestDto;
import com.daily_app.demo.Dto.Request.ReportUpdateRequestDto;
import com.daily_app.demo.Dto.Response.DailyResponseDto;
import com.daily_app.demo.Dto.Response.WeeklyListResponseDto;
import com.daily_app.demo.Service.DailyCrudService;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class DailyController {

    @Autowired
    private DailyCrudService dailyCrud;

        @GetMapping("/weekly_list/{user_id}")
        public WeeklyListResponseDto getWeeklyList(@PathVariable Integer user_id) {
            return dailyCrud.weeklyListResponse(user_id);
        }

        @GetMapping("/daily/{userId}/{startDate}/{endDate}")
        public DailyResponseDto getDaily(
                @PathVariable Integer userId,
                @PathVariable LocalDate startDate,
                @PathVariable LocalDate endDate) {
                return dailyCrud.dailyResponse(userId, startDate, endDate);
        }

        @PostMapping("/report")
        public Map<String, String> createReport(@RequestBody ReportRequestDto request) {
            return dailyCrud.reportDaily(request);
        }

        @PutMapping("/update")
        public Map<String, String> updateReport(@RequestBody ReportUpdateRequestDto request) {
            return dailyCrud.updateDaily(request);
        }

        @DeleteMapping("/delete/{daily_id}")
        public Map<String, String> deleteReport(@PathVariable Integer daily_id) {
            return dailyCrud.deleteDaily(daily_id);
        }
        

}
