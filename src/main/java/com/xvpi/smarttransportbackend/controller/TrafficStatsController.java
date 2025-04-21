package com.xvpi.smarttransportbackend.controller;
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
    //    private final CameraDeviceRepository cameraDevcordService;
////    private final RoadSectionRepository roadSectiiceRepository;
//    @ApiOperation("获取最近一小时的车流统计数据")
//    @GetMapping("/traffic/hourly-stats")
//    public ApiResponse<Map<String, Long>> getHourlyStats() {
//        return ApiResponse.success(vehicleRecordService.getVehicleCountByRoadLastHour());
//    }
//    @ApiOperation("获取路径GIS坐标数据")
//    @GetMapping("/road/path")
//    public ApiResponse<String> getPathByRoadNo(@RequestParam String roadNo) {
//        Optional<RoadSection> section = roadSectionRepository.findByOName(roadNo);
//        return section.map(s -> ApiResponse.success(s.getPathGis()))
//                .orElseGet(() -> ApiResponse.error("未找到路径信息"));
//    }
//    @ApiOperation("获取全部摄像头的运行状态")
//    @GetMapping("/cameras/status")
//    public ApiResponse<List<CameraDevice>> getAllCameraStatus() {
//        return ApiResponse.success(cameraDeviceRepository.findAll());
//    }
@GetMapping("/type-count")
@ApiOperation("统计给定时间前10分钟内各类型车辆数量")
//public ApiResponse<?> getVehicleTypeCount(@RequestParam String timeStr) {
//    return ApiResponse.success(vehicleRecordService.getVehicleTypeCountInLastTenMinutes(timeStr));
//}
public ApiResponse<?> getVehicleTypeCount(@RequestParam String timeStr) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime endTime = LocalDateTime.parse(timeStr, formatter);
    LocalDateTime startTime = endTime.minusMinutes(10);

    Map<String, Long> result = vehicleRecordService.getVehicleTypeCount(startTime, endTime);
    return ApiResponse.success(result);
}
}

