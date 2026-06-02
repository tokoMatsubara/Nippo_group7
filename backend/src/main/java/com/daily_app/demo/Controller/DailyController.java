package com.daily_app.demo.Controller;

import org.springframework.web.bind.annotation.*;

import com.daily_app.demo.Dto.Response.WeeklyListResponseDto;

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
                "2026-06-01",
                "2026-06-07",
                "ログイン機能・日報一覧を実装"));

        list.add(new WeeklyListResponseDto.SummaryDto(
                "2026-06-08",
                "2026-06-14",
                "ダッシュボード・リマインド機能を実装"));

        return new WeeklyListResponseDto(list);
    }
}