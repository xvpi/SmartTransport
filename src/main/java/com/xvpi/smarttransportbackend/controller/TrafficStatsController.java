package com.xvpi.smarttransportbackend.controller;
import com.xvpi.smarttransportbackend.config.PythonRunner;
import com.xvpi.smarttransportbackend.entity.ApiResponse;
import com.xvpi.smarttransportbackend.service.VehicleRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.*;



@RestController
@RequestMapping("/api/screen")
@Api(tags = "大屏")
public class TrafficStatsController {
    @Autowired
    private VehicleRecordService vehicleRecordService;
@GetMapping("/type-count")
@ApiOperation("统计给定时间前10分钟内各类型车辆数量")
public ApiResponse<?> getVehicleTypeCount(@RequestParam String timeStr) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime endTime = LocalDateTime.parse(timeStr, formatter);
    LocalDateTime startTime = endTime.minusMinutes(10);

    Map<String, Long> result = vehicleRecordService.getVehicleTypeCount(startTime, endTime);
    return ApiResponse.success(result);
}

    @GetMapping("/averageSpeed")
    @ApiOperation("统计给定时间前10分钟内平均车速")
    public ApiResponse<?> getAverageSpeed(@RequestParam String time) {
        try {
            // Python 脚本路径（绝对路径或相对路径）
            String scriptPath = "python/classify_model/averageSpeedCount.py";

            // 调用 Python 脚本，传入时间参数
            List<String> output = PythonRunner.runPythonScript(scriptPath, time);

            // 一般就一行输出
            if (output.isEmpty()) {
                return ApiResponse.error("Python 脚本没有输出");
            }

            String result = output.get(0); // 平均速度
            Map<String, Object> response = new HashMap<>();
            response.put("time", time);
            response.put("averageSpeed", Double.parseDouble(result));

            return ApiResponse.success(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("获取平均速度失败: " + e.getMessage());
        }
    }


}

