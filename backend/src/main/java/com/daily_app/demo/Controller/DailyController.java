package com.daily_app.demo.Controller;

import org.springframework.web.bind.annotation.*;

import com.daily_app.demo.Dto.Response.CategoryDto;
import com.daily_app.demo.Dto.Response.DailyDto;
import com.daily_app.demo.Dto.Response.DailyResponseDto;
import com.daily_app.demo.Dto.Response.WeeklyListResponseDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
                        new CategoryDto(1, "学び", "設計を実施"),
                        new CategoryDto(2, "課題点", "API作成")));

        DailyDto tuesday = new DailyDto(
                start.plusDays(1),
                List.of(
                        new CategoryDto(1, "学び", "フロント接続"),
                        new CategoryDto(2, "課題点", "バグ修正")));

        return new DailyResponseDto(
                start,
                end,
                List.of(monday, tuesday));
    }
}