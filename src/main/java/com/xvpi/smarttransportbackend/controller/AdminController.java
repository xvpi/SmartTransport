package com.xvpi.smarttransportbackend.controller;

import com.alibaba.excel.EasyExcel;
import com.xvpi.smarttransportbackend.dao.DispatchTaskDao;
import com.xvpi.smarttransportbackend.entity.*;
import com.xvpi.smarttransportbackend.repository.DispatchTaskRepository;
import com.xvpi.smarttransportbackend.repository.TrafficOfficerRepository;
import com.xvpi.smarttransportbackend.service.CameraDeviceService;
import com.xvpi.smarttransportbackend.service.EmergencyReportService;
import com.xvpi.smarttransportbackend.service.RoadSectionService;
import com.xvpi.smarttransportbackend.service.TrafficOfficerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@Api(tags = "管理员端")
public class AdminController {
    @Autowired
    private TrafficOfficerService officerService;
    @Autowired
    private CameraDeviceService cameraDeviceService;
    @Autowired
    private RoadSectionService roadSectionService;
    @Autowired
    private DispatchTaskRepository dispatchTaskRepository;
    @Autowired
    private DispatchTaskDao dispatchTaskDao;
    @GetMapping("/officer/list")
    @ApiOperation("列出所有的交警信息")
    public ApiResponse<List<TrafficOfficer>> list() {
        return ApiResponse.success(officerService.getAllOfficers());
    }

    @PostMapping("/officer/add")
    @ApiOperation("加入交警信息")
    public ApiResponse<TrafficOfficer> add(@RequestBody TrafficOfficer officer) {
        return ApiResponse.success(officerService.addOfficer(officer));
    }


    @PutMapping("/officer/reset-password")
    @ApiOperation("重置密码")
    public ApiResponse<?> resetPassword(@RequestParam Long id, @RequestParam String newPassword) {
        try {
            officerService.resetPassword(id, newPassword);
            return ApiResponse.success("重置密码成功");
        } catch (Exception e) {
            return ApiResponse.error("重置密码失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/officer/delete/{id}")
    @ApiOperation("删除账户信息")
    public ApiResponse<?> delete(@PathVariable Long id) {
        try {
            officerService.deleteOfficer(id);
            return ApiResponse.success("删除账户成功");
        } catch (Exception e) {
            return ApiResponse.error("删除账户失败: " + e.getMessage());
        }
    }
    @GetMapping("/camara/list")
    @ApiOperation("列出所有的摄像头信息")
    public ApiResponse<List<CameraDevice>> listCameras() {
        return ApiResponse.success(cameraDeviceService.getAllDevices());
    }

    @PostMapping("/camara/add")
    @ApiOperation("加入摄像头信息")
    public ApiResponse<CameraDevice> addCamera(@RequestBody CameraDevice cameraDevice) {
        return ApiResponse.success(cameraDeviceService.addCamera(cameraDevice));
    }
    @PutMapping("/camara/update")
    @ApiOperation("更换摄像头信息")
    public ApiResponse<?> updateCamera(@RequestBody CameraDevice cameraDevice) {
        try {
            cameraDeviceService.updateCamera(cameraDevice);
            return ApiResponse.success("更新摄像头信息成功");
        } catch (Exception e) {
            return ApiResponse.error("摄像头信息失败: " + e.getMessage());
        }
    }


    @DeleteMapping("/camara/delete/{id}")
    @ApiOperation("删除摄像头信息")
    public ApiResponse<?> deleteCamera(@PathVariable Integer id) {
        try {
            cameraDeviceService.deleteCamera(id);
            return ApiResponse.success("删除摄像头成功");
        } catch (Exception e) {
            return ApiResponse.error("删除摄像头失败: " + e.getMessage());
        }
    }

    @GetMapping("/road/list")
    @ApiOperation("列出所有的路段信息")
    public ApiResponse<List<RoadSection>> listRoads() {
        return ApiResponse.success(roadSectionService.getAll());
    }

    @PostMapping("/road/add")
    @ApiOperation("加入路段信息")
    public ApiResponse<RoadSection> addRoad(@RequestBody RoadSection roadSection) {
        return ApiResponse.success(roadSectionService.addRoad(roadSection));
    }
    @PutMapping("/road/update")
    @ApiOperation("更换路段信息")
    public ApiResponse<?> updateRoad(@RequestBody RoadSection roadSection) {
        try {
            roadSectionService.updateRoad(roadSection);
            return ApiResponse.success("更新路段信息成功");
        } catch (Exception e) {
            return ApiResponse.error("路段信息失败: " + e.getMessage());
        }
    }


    @DeleteMapping("/road/delete/{id}")
    @ApiOperation("删除路段信息")
    public ApiResponse<?> deleteRoad(@PathVariable Integer id) {
        try {
            roadSectionService.deleteRoad(id);
            return ApiResponse.success("删除路段成功");
        } catch (Exception e) {
            return ApiResponse.error("删除路段失败: " + e.getMessage());
        }
    }

    @GetMapping("/dispatch-tasks/getAll")
    @ApiOperation("获取所有分配任务")
    public ApiResponse<List<DispatchTask>> getAllTasks() {
        try {
            return ApiResponse.success(dispatchTaskRepository.findAll());
        } catch (Exception e) {
            return ApiResponse.error("删除账户失败: " + e.getMessage());
        }
    }
    @GetMapping("/dispatch-tasks/filter")
    @ApiOperation("按交警、时间、状态筛选任务yyyy-mm-ssThh:mm:Ss")
    public ApiResponse<List<DispatchTask>> filterTasks(
            @RequestParam(required = false) Long officerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) Integer status
    ) {
        try {
            return  ApiResponse.success(dispatchTaskDao.findByCondition(officerId, start, end, status));
        } catch (Exception e) {
            return ApiResponse.error("筛选任务失败: " + e.getMessage());
        }

    }
    @Autowired
    TrafficOfficerRepository trafficOfficerRepository;
    @PutMapping("/dispatch-tasks/assign-task")
    @ApiOperation("分配commend给多个交警")
    public ApiResponse<String> assignTask(
            @RequestParam Long commandId,
            @RequestParam List<Long> officerIds) {

        for (Long officerId : officerIds) {
            DispatchTask task = new DispatchTask();
            task.setCommandId(commandId);
            task.setOfficerId(officerId);
            task.setAssignTime(LocalDateTime.now());
            task.setStatus(0); // 未开始
            dispatchTaskRepository.save(task);

            // 修改交警状态为 2（分配中）
            trafficOfficerRepository.findById(officerId).ifPresent(officer -> {
                officer.setStatus(2);
                trafficOfficerRepository.save(officer);
            });
        }

        return ApiResponse.success("任务分配成功");
    }
    @Autowired
    EmergencyReportService reportService;
    @GetMapping("/emergency-reports/all-reports")
    @ApiOperation("获取所有事故报告")
    public ApiResponse<List<EmergencyReport>>getReports(
            @RequestParam(required = false) Long officerId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        try {
            return  ApiResponse.success(reportService.getReports(officerId, status, type, start, end));
        } catch (Exception e) {
            return ApiResponse.error("获取事故报告失败: " + e.getMessage());
        }
    }

    @GetMapping("/emergency-reports/type")
    @ApiOperation("计数不同类型的事故报告")
    public ApiResponse<Map<String, Integer>> countByType() {
        try {
            return  ApiResponse.success(reportService.countByType());
        } catch (Exception e) {
            return ApiResponse.error("获取事故报告失败: " + e.getMessage());
        }
    }

    @GetMapping("/emergency-reports/heatmap")
    @ApiOperation("获取事故报告热力图数据")
    public ApiResponse<List<Map<String, Object>>> getHeatmapData() {
        // 获取所有事故报告的经纬度数据
        List<EmergencyReport> reports = reportService.getHeatmapPoints();

        // 创建一个 Map 来聚合相同经纬度的 count 值
        Map<String, Integer> heatmapData = new HashMap<>();

        for (EmergencyReport report : reports) {
            // 使用经纬度拼接成一个字符串作为键
            String key = report.getLocationLat() + "," + report.getLocationLng();
            heatmapData.put(key, heatmapData.getOrDefault(key, 0) + 1);
        }

        // 将结果转换为需要的格式
        return ApiResponse.success(heatmapData.entrySet().stream()
                .map(entry -> {
                    String[] latLng = entry.getKey().split(",");
                    Map<String, Object> map = new HashMap<>();
                    map.put("lat", Double.parseDouble(latLng[0]));
                    map.put("lng", Double.parseDouble(latLng[1]));
                    map.put("count", entry.getValue());
                    return map;
                })
                .collect(Collectors.toList()));
    }

    @GetMapping("/emergency-reports/export")
    @ApiOperation("导出事故报告到表格")
    public void exportToExcel(HttpServletResponse response,
                              @RequestParam(required = false) Long officerId,
                              @RequestParam(required = false) String status,
                              @RequestParam(required = false) String type,
                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) throws IOException {

         reportService.exportToExcel(response,officerId,status,type,start,end);
    }

    @PostMapping("/emergency-reports/respond")
    @ApiOperation("指挥端回应事故报告")
    public ApiResponse<String> respondToReport(
            @RequestParam Long reportId,
            @RequestParam String status,
            @RequestParam(required = false) String response) {

        try {
            boolean success = reportService.respondToReport(reportId, status, response);
            return success ? ApiResponse.success("回应成功") : ApiResponse.error("未找到对应事故报告");
        } catch (Exception e) {
            return ApiResponse.error("回应失败: " + e.getMessage());
        }
    }


}
