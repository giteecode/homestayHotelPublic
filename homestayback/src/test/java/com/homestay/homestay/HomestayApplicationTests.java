package com.homestay.homestay;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;

@SpringBootTest
class HomestayApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void dateTest(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss");

        LocalDateTime yearStart = now.with(TemporalAdjusters.firstDayOfYear()).with(LocalTime.MIN);
        LocalDateTime yearEnd = now.with(TemporalAdjusters.lastDayOfYear()).with(LocalTime.MAX);
        System.out.println("当前年的开始时间:" + yearStart.format(fmt));
        System.out.println("当前年的结束时间:" + yearEnd.format(fmt));

        LocalDateTime monthStart = now.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
        LocalDateTime monthEnd = now.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
        System.out.println("当前月的开始时间:" + monthStart.format(fmt));
        System.out.println("当前月的结束时间:" + monthEnd.format(fmt));

        int dayOfWeek = now.getDayOfWeek().getValue();
        LocalDateTime weekStart = now.minusDays(dayOfWeek - 1).with(LocalTime.MIN);
        LocalDateTime weekEnd = now.plusDays(7 - dayOfWeek).with(LocalTime.MAX);
        System.out.println("当前周的开始时间:" + weekStart.format(fmt));
        System.out.println("当前周的结束时间:" + weekEnd.format(fmt));

        LocalDateTime dayStart = now.with(LocalTime.MIN);
        LocalDateTime dayEnd = now.with(LocalTime.MAX);
        System.out.println("当天的开始时间:" + dayStart.format(fmt));
        System.out.println("当天的结束时间:" + dayEnd.format(fmt));
    }

    /**
     * 获取当前月及之前的月份
     */
    @Test
    void monthTest(){
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        System.out.println("当前月份："+month);
        for (int i = 1; i < month; i++) {
            System.out.println("剩余月份："+i);
        }

    }

    @Test
    void getTimeList(){
        LocalDateTime now = LocalDateTime.now(); // 获取当前时间
        int year = now.getYear(); // 获取年份
        int month = now.getMonthValue(); // 获取月份
        int day = now.getDayOfMonth(); // 获取月中的天数
        int hour = now.getHour(); // 获取当前的小时
        int minute = now.getMinute(); // 获取当前分钟
        int second = now.getSecond(); // 获取当前秒数
        System.out.println(year);
        System.out.println(month);
        System.out.println(day);
        System.out.println(hour);
        System.out.println(minute);
        System.out.println(second);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss");
        int week = now.getDayOfWeek().getValue();
        System.out.println("周："+week);
        int dayOfWeek = now.getDayOfWeek().getValue();
        LocalDateTime weekStart = now.minusDays(dayOfWeek - 1).with(LocalTime.MIN);
        LocalDateTime weekEnd = now.plusDays(7 - dayOfWeek).with(LocalTime.MAX);
        System.out.println("当前周的开始时间:" + weekStart.format(fmt));
        System.out.println("当前周的结束时间:" + weekEnd.format(fmt));
        int weekOfYear = LocalDateTimeUtil.weekOfYear(now);
        System.out.println("当前周数："+weekOfYear);
        // 获取上周
        now = now.minusDays(14);
        weekStart = now.minusDays(dayOfWeek - 1).with(LocalTime.MIN);
        weekEnd = now.plusDays(7 - dayOfWeek).with(LocalTime.MAX);
        System.out.println("上上周的开始时间:" + weekStart.format(fmt));
        System.out.println("上上周的结束时间:" + weekEnd.format(fmt));
        weekOfYear = LocalDateTimeUtil.weekOfYear(now);
        System.out.println("上上周数："+weekOfYear);
    }

    @Test
    void minusMonth(){
        // 1.先获取当前月
        LocalDateTime now = LocalDateTime.now(); // 获取当前时间
        int nowMonth = now.getMonthValue();
        for (int i = 0; i < nowMonth; i++) {
            // 2.月份递减
            LocalDateTime newTime = now.minusMonths(i);
            System.out.println(newTime);
        }
    }

    @Test
    void resetPWD(){
        System.out.println(RandomUtil.randomString(10));
    }

}
