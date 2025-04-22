package com.xvpi.smarttransportbackend.controller;


import com.xvpi.smarttransportbackend.entity.ApiResponse;
import com.xvpi.smarttransportbackend.entity.TrafficOfficer;
import com.xvpi.smarttransportbackend.service.TrafficOfficerService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/officer")
@Api(tags = "管理员端")
public class AdminController {
    @Autowired
    private TrafficOfficerService officerService;

    @GetMapping("/list")
    public ApiResponse<List<TrafficOfficer>> list() {
        return ApiResponse.success(officerService.getAllOfficers());
    }

    @PostMapping("/add")
    public ApiResponse<TrafficOfficer> add(@RequestBody TrafficOfficer officer) {
        return ApiResponse.success(officerService.addOfficer(officer));
    }


    @PutMapping("/reset-password")
    public ApiResponse<?> resetPassword(@RequestParam Long id, @RequestParam String newPassword) {
        try {
            officerService.resetPassword(id, newPassword);
            return ApiResponse.success("重置密码成功");
        } catch (Exception e) {
            return ApiResponse.error("重置密码失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<?> delete(@PathVariable Long id) {
        try {
            officerService.deleteOfficer(id);
            return ApiResponse.success("删除账户成功");
        } catch (Exception e) {
            return ApiResponse.error("删除账户失败: " + e.getMessage());
        }
    }


}
