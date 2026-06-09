package com.daily_app.demo.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
 
public class BusinessDayUtil {
 
    public static LocalDate previousBusinessDay(LocalDate date) {

        LocalDate result = date.minusDays(1);

        while (isWeekend(result)) {

            result = result.minusDays(1);

        }

        return result;

    }
 
    private static boolean isWeekend(LocalDate date) {

        DayOfWeek dow = date.getDayOfWeek();

        return dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY;

    }

}
 