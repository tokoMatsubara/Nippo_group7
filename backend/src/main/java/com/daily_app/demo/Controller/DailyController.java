package com.daily_app.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.daily_app.demo.Dto.Request.ReportRequestDto;
import com.daily_app.demo.Dto.Request.ReportUpdateRequestDto;
import com.daily_app.demo.Dto.Response.ContentDto;
import com.daily_app.demo.Dto.Response.DailyDto;
import com.daily_app.demo.Dto.Response.DailyResponseDto;
import com.daily_app.demo.Dto.Response.WeeklyListResponseDto;
import com.daily_app.demo.Service.DailyCrudService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class DailyController {

    @Autowired
    DailyCrudService dailyCrud;


    @GetMapping("/weekly_list/")
    public WeeklyListResponseDto getWeeklyList(@PathVariable Long user_id) {
        return dailyCrud.weeklyListResponse(1);

        // List<WeeklyListResponseDto.SummaryDto> list = new ArrayList<>();

        // list.add(new WeeklyListResponseDto.SummaryDto(
        //                 LocalDate.of(2026, 6, 1),
        //                 LocalDate.of(2026, 6, 7),
        //                 "ログイン機能・日報一覧を実装"));

        // list.add(new WeeklyListResponseDto.SummaryDto(
        //                 LocalDate.of(2026, 6, 8),
        //                 LocalDate.of(2026, 6, 14),
        //                 "ダッシュボード・リマインド機能を実装"));

        //return new WeeklyListResponseDto(list);
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
                                            new ContentDto(2, "課題点", "API作成")),
                            "設計とAPI作成の基本構造を理解し実装を開始");

            DailyDto tuesday = new DailyDto(
                            start.plusDays(1),
                            List.of(
                                            new ContentDto(1, "学び", "フロント接続"),
                                            new ContentDto(2, "課題点", "バグ修正")),
                            "フロントとAPIの接続確認と不具合修正を実施");

            return new DailyResponseDto(
                            start,
                            end,
                            List.of(monday, tuesday));
    }

    @PostMapping("/report")
    public Map<String, Object> createReport(@RequestBody ReportRequestDto request) {

            System.out.println("userId: " + request.getUserId());
            System.out.println("date: " + request.getDate());
            System.out.println("contents size: " + request.getContents().size());

            for (ReportRequestDto.ContentDto c : request.getContents()) {
                    System.out.println("categoryId: " + c.getCategoryId());
                    System.out.println("content: " + c.getContent());
            }

            return Map.of(
                            "status", "success",
                            "message", "日報登録成功（モック）");
    }

    @PutMapping("/update")
    public Map<String, Object> updateReport(
                    @RequestBody ReportUpdateRequestDto request) {

            System.out.println("daily_id: " + request.getDailyId());
            System.out.println("contents size: " + request.getContents().size());

            return Map.of(
                            "status", "success",
                            "message", "日報更新成功（モック）");
    }

    @DeleteMapping("/delete/{daily_id}")
    public Map<String, Object> deleteReport(
                    @PathVariable Long daily_id) {

            System.out.println("削除対象 daily_id: " + daily_id);

            return Map.of(
                            "status", "success",
                            "message", "日報削除成功（モック）");
    }

}
