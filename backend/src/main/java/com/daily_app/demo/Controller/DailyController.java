package com.daily_app.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.daily_app.demo.Dto.Request.ReportRequestDto;
import com.daily_app.demo.Dto.Request.ReportUpdateRequestDto;
import com.daily_app.demo.Dto.Response.DailyResponseDto;
import com.daily_app.demo.Dto.Response.WeeklyListResponseDto;
import com.daily_app.demo.Service.DailyCrudService;
// import com.daily_app.demo.Service.DailySummaryService;
import com.daily_app.demo.config.CustomUserDetails;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class DailyController {

    @Autowired
    private DailyCrudService dailyCrud;

    // @Autowired
    // private DailySummaryService dailySummaryService;

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
    public Map<String, String> createReport(
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

    // @PostMapping("/summary")
    // public Map<String, String> generateSummary(@RequestBody ReportRequestDto
    // request) {
    // String summary = dailySummaryService.generateSummary(request);
    // // 他のメソッドに合わせて Map<String, String>（JSON形式）で返却します
    // return Collections.singletonMap("summary", summary);
    // }

}
