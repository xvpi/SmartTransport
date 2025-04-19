package com.xvpi.smarttransportbackend.controller;
import com.xvpi.smarttransportbackend.service.VehicleRecordService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@Slf4j
@RestController
@RequestMapping("/api/screen")
@Api(tags = "大屏-流量统计")
public class TrafficStatsController {
//@Autowired
//private final VehicleRecordService vehicleRecordService;
//    private final RoadSectionRepository roadSectionRepository;
//    private final CameraDeviceRepository cameraDeviceRepository;
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


}

