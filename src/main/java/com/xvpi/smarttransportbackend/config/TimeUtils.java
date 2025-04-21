package com.xvpi.smarttransportbackend.config;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

    public static int convertTimeStrToIndex(String timeStr) {
        // 格式：yyyy/MM/dd HH:mm
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime startOfDay = LocalDateTime.of(2024, 12, 29, 0, 0,0);
        LocalDateTime inputTime = LocalDateTime.parse(timeStr, formatter);

        long minutes = Duration.between(startOfDay, inputTime).toMinutes();
        return (int) (minutes / 10); // 每10分钟一个时间段
    }
    public static String convertIndexToTimeStr(int index) {
        LocalDateTime start = LocalDateTime.of(2024, 12, 29, 0, 0,0);
        LocalDateTime time = start.plusMinutes(index * 10L);
        return time.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }

}
