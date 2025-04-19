package com.xvpi.smarttransportbackend.controller;

import com.xvpi.smarttransportbackend.entity.ApiResponse;
import com.xvpi.smarttransportbackend.entity.CameraDevice;
import com.xvpi.smarttransportbackend.service.CameraDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/camera-device")
@Api(tags = "传感器信息")
public class CameraDeviceController {
    @Autowired
    private final CameraDeviceService cameraDeviceService;

    public CameraDeviceController(CameraDeviceService cameraDeviceService) {
        this.cameraDeviceService = cameraDeviceService;
    }
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
    @GetMapping("/all")
    @ApiOperation("获取所有传感器数据")
    public ApiResponse<List<CameraDevice>> getAll() {
        return ApiResponse.success(cameraDeviceService.getAllDevices());
    }

    @GetMapping
    @ApiOperation("根据简称获取传感器数据")
    public ApiResponse<CameraDevice> getByShortName(@RequestParam String shortName) {
        Optional<CameraDevice> device = cameraDeviceService.getByShortName(shortName);
        return device.map(d -> ApiResponse.success(d))
                .orElseGet(() -> ApiResponse.error("未找到传感器信息"));
    }
}
