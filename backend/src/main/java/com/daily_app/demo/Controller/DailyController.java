package com.daily_app.demo.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.daily_app.demo.Dto.Request.ReportRequestDto;
import com.daily_app.demo.Dto.Request.ReportUpdateRequestDto;
import com.daily_app.demo.Dto.Response.DailyResponseDto;
import com.daily_app.demo.Dto.Response.PreviousGoalResponseDto;
import com.daily_app.demo.Dto.Response.WeeklyListResponseDto;
import com.daily_app.demo.Service.DailyCrudService;
// import com.daily_app.demo.Service.DailySummaryService;
import com.daily_app.demo.config.CustomUserDetails;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DailyController {

    private final DailyCrudService dailyCrud;

    public DailyController(DailyCrudService dailyCrudService) {
        this.dailyCrud = dailyCrudService;
    }

    @GetMapping("/weekly_list")
    public WeeklyListResponseDto getWeeklyList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return dailyCrud.weeklyListResponse(userDetails.getId());
    }

    @GetMapping("/daily/{startDate}/{endDate}")
    public DailyResponseDto getDaily(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable LocalDate startDate,
            @PathVariable LocalDate endDate) {
        return dailyCrud.dailyResponse(userDetails.getId(), startDate, endDate);
    }

    @PostMapping("/report")
    public ResponseEntity<Map<String, String>> createReport(
            @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ReportRequestDto request) {
        return dailyCrud.reportDaily(userDetails.getUser(), request);
    }

    @PutMapping("/update")
    public Map<String, String> updateReport(
            @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ReportUpdateRequestDto request) {
        return dailyCrud.updateDaily(userDetails.getUser(), request);
    }

    @DeleteMapping("/delete/{daily_id}")
    public Map<String, String> deleteReport(
            @AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Integer daily_id) {
        return dailyCrud.deleteDaily(userDetails.getUser(), daily_id);
    }

    @GetMapping("/daily/previous-goal")
    public PreviousGoalResponseDto getPreviousGoal1(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        return dailyCrud.previousGoal(
                userDetails.getUser());
    }

}
