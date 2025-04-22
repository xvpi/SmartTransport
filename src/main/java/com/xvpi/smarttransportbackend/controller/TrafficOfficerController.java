package com.xvpi.smarttransportbackend.controller;

import com.xvpi.smarttransportbackend.entity.*;
import com.xvpi.smarttransportbackend.repository.DispatchTaskRepository;
import com.xvpi.smarttransportbackend.service.EmergencyReportService;
import com.xvpi.smarttransportbackend.service.TrafficOfficerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Result;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/police")
@Api(tags = "交警信息")
public class TrafficOfficerController {
    @Autowired
    private TrafficOfficerService officerService;
    @Autowired
    private EmergencyReportService reportService;
    @PostMapping("/register")
    @ApiOperation("注册")
    public ApiResponse<TrafficOfficer> register(@RequestBody TrafficOfficer officer) {
        TrafficOfficer saved = officerService.register(officer);
        return ApiResponse.success(saved);
    }

    @PostMapping("/login")
    @ApiOperation("登录")
    public ApiResponse<?> login(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = payload.get("password");
        String token = officerService.login(username, password);
        return ApiResponse.success(Collections.singletonMap("token", token));
    }

    @GetMapping("/me")
    @ApiOperation("返回自身(bearer+JWT)")
    public ApiResponse<?> currentUser(HttpServletRequest request) {
        TrafficOfficer officer = officerService.getCurrentOfficer(request);
        try {
            return ApiResponse.success(officer);
        } catch (Exception e) {
            return ApiResponse.error("查询失败: " + e.getMessage());
        }
    }
    @PostMapping("/updateposition/{id}")
    @ApiOperation("交警更新自身位置")
    public ResponseEntity<String> updatePositionById(@PathVariable Long id,
                                                     @RequestParam Double currentLat,
                                                     @RequestParam Double currentLng) {
        try {
            officerService.updatePositionById(id, currentLat, currentLng);
            return ResponseEntity.ok("Position updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update position");
        }
    }
//    @PostMapping("/assign")
//    @ApiOperation("根据AI指令就近分配任务给空闲交警")
//    public ApiResponse<Boolean> assignTask(@RequestParam Long commandId) {
//        boolean result = officerService.assignTask(commandId);
//        return ApiResponse.success(result);
//    }
    @PostMapping("/cancelTask")
    @ApiOperation("交警取消任务")
    public ApiResponse<String> cancelTask(@RequestParam Long taskId) {
        officerService.cancelTask(taskId);
        return ApiResponse.success("任务已取消");
    }

    @PostMapping("/acceptTask")
    @ApiOperation("交警接受任务")
    public ApiResponse<String> acceptTask(@RequestParam Long taskId) {
        officerService.acceptTask(taskId);
        return ApiResponse.success("任务已接受");
    }
    @GetMapping("/CheckTask")
    @ApiOperation("交警查看自己的所有任务")
    public ApiResponse<?> getCurrentTasks(HttpServletRequest httpRequest) {
        TrafficOfficer officer = officerService.getCurrentOfficer(httpRequest);
        Long officerId = officer.getId();
        List<DispatchTask> assignedTasks = officerService.getCurrentTasks(officerId);
        return ApiResponse.success(Map.of(
                "officer", officer,
                "task", assignedTasks
        ));
    }
    @PostMapping("/report")
    @ApiOperation("交警上传故障信息")
    public ApiResponse<?> reportEmergency(@RequestParam String type,
                                     @RequestParam(required = false) String description,
                                     @RequestParam(required = false) MultipartFile photo,
                                     HttpServletRequest request) throws IOException {
        reportService.submit(type, description, photo, request);
        return ApiResponse.success("上报成功");
    }

    @GetMapping("/myreport")
    @ApiOperation("交警查看自己上传的故障报告")
    public ApiResponse<?> getMyReports(HttpServletRequest request) {
        return ApiResponse.success(reportService.getMyReports(request));
    }
}
