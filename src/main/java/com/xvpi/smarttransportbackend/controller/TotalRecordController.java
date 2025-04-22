package com.xvpi.smarttransportbackend.controller;
import com.xvpi.smarttransportbackend.entity.ApiResponse;
import com.xvpi.smarttransportbackend.entity.TotalRecord;
import com.xvpi.smarttransportbackend.service.TotalRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController

@RequestMapping("/api/total-record")
@Api(tags = "出入车辆数信息")
public class TotalRecordController {

    @Autowired
    private TotalRecordService totalRecordService;

    @GetMapping("/all")
    @ApiOperation("获取所有出入车辆数数据")
    public ApiResponse<List<TotalRecord>>getAllRecords() {
        return ApiResponse.success(totalRecordService.getAllRecords());
    }

    @GetMapping("/query")
    @ApiOperation("通过时间段查询出入车辆数 yyyy-mm-ddThh:mm:ss")
    public ApiResponse<List<TotalRecord>> getRecordsByTimeRange(
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime) {
            LocalDateTime start = LocalDateTime.parse(startTime);
            LocalDateTime end = LocalDateTime.parse(endTime);
        try {
            return ApiResponse.success(totalRecordService.getRecordsByTimeRange(start, end));
        } catch (Exception e) {
            return ApiResponse.error("查询失败: " + e.getMessage());
        }
    }
    @GetMapping("/previous")
    @ApiOperation("根据时间获取上一段的进出计数")
    public ApiResponse<?> getPreviousRecord(@RequestParam String timeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime inputTime = LocalDateTime.parse(timeStr, formatter);

        TotalRecord record = totalRecordService.getPreviousRecord(inputTime);
        if (record == null) {
            return ApiResponse.error("无对应记录");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("startTime", record.getStartTime());
        result.put("endTime", record.getEndTime());
        result.put("leaveCount", record.getLeaveCount());
        result.put("enterCount", record.getEnterCount());

        return ApiResponse.success(result);
    }

    @GetMapping("/plate-count")
    @ApiOperation("统计某日出现的不同车数yyyy/mm/dd")
    public ApiResponse<Integer> getLaneCount(@RequestParam String date) {
        int count = totalRecordService.countDistinctPlateNoByDate(date);
        return ApiResponse.success(count);
    }

}
