package com.daily_app.demo.Controller;

import org.springframework.web.bind.annotation.*;

import com.daily_app.demo.Dto.Request.ReportRequestDto;
import com.daily_app.demo.Dto.Request.ReportUpdateRequestDto;
import com.daily_app.demo.Dto.Response.ContentDto;
import com.daily_app.demo.Dto.Response.DailyDto;
import com.daily_app.demo.Dto.Response.DailyResponseDto;
import com.daily_app.demo.Dto.Response.WeeklyListResponseDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class DailyController {

    @GetMapping("/weekly_list/{user_id}")
    public WeeklyListResponseDto getWeeklyList(@PathVariable Long user_id) {

        List<WeeklyListResponseDto.SummaryDto> list = new ArrayList<>();

        list.add(new WeeklyListResponseDto.SummaryDto(
                LocalDate.of(2026, 6, 1),
                LocalDate.of(2026, 6, 7),
                "ログイン機能・日報一覧を実装"));

        list.add(new WeeklyListResponseDto.SummaryDto(
                LocalDate.of(2026, 6, 8),
                LocalDate.of(2026, 6, 14),
                "ダッシュボード・リマインド機能を実装"));
        return new WeeklyListResponseDto(list);
    }

    @GetMapping("/daily/{userId}/{startDate}/{endDate}")
    public DailyResponseDto getDaily(
            @PathVariable int userId,
            @PathVariable String startDate,
            @PathVariable String endDate) {

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        // --- モックデータ ---
        DailyDto monday = new DailyDto(
                start,
                List.of(
                        new ContentDto(1, "学び", "設計を実施"),
                        new ContentDto(2, "課題点", "API作成")));

        DailyDto tuesday = new DailyDto(
                start.plusDays(1),
                List.of(
                        new ContentDto(1, "学び", "フロント接続"),
                        new ContentDto(2, "課題点", "バグ修正")));

        return new DailyResponseDto(
                start,
                end,
                List.of(monday, tuesday));
    }

    @PostMapping("/report")
    public Map<String, Object> createReport(@RequestBody ReportRequestDto request) {

        System.out.println("userId: " + request.getUserId());
        System.out.println("date: " + request.getDate());
        System.out.println("contents: " + request.getContents().size());

        return Map.of(
                "status", "success",
                "message", "日報登録成功（モック）");
    }

    @PutMapping("/update/{dailyId}")
    public Map<String, Object> updateReport(
            @PathVariable Long dailyId,
            @RequestBody ReportUpdateRequestDto request) {

        System.out.println("dailyId: " + dailyId);
        System.out.println("date: " + request.getDate());
        System.out.println("contents size: " + request.getContents().size());

        return Map.of(
                "status", "success",
                "message", "日報更新成功（モック）");
    }

}