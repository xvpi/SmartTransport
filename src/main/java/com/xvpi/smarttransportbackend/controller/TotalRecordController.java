package com.xvpi.smarttransportbackend.controller;
import com.xvpi.smarttransportbackend.entity.TotalRecord;
import com.xvpi.smarttransportbackend.service.TotalRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController

@RequestMapping("/api/total-record")
@Api(tags = "出入车辆数信息")
public class TotalRecordController {

    @Autowired
    private TotalRecordService totalRecordService;

    @GetMapping("/all")
    @ApiOperation("获取所有出入车辆数数据")
    public List<TotalRecord> getAllRecords() {
        return totalRecordService.getAllRecords();
    }

    @GetMapping("/query")
    @ApiOperation("通过时间段查询出入车辆数 yyyy-mm-ddThh:mm:ss")
    public List<TotalRecord> getRecordsByTimeRange(
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime) {
            LocalDateTime start = LocalDateTime.parse(startTime);
            LocalDateTime end = LocalDateTime.parse(endTime);
        return totalRecordService.getRecordsByTimeRange(start, end);
    }
}
