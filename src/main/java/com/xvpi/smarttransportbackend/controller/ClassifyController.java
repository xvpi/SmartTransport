package com.xvpi.smarttransportbackend.controller;


import com.xvpi.smarttransportbackend.entity.ApiResponse;
import com.xvpi.smarttransportbackend.service.ClassifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/classify")
@Api(tags = "分类信息")
public class ClassifyController {

    @Autowired
    private ClassifyService classifyService;

    @GetMapping("/all")
    @ApiOperation("获取全部分类数据")
    public ApiResponse<List<List<Integer>>> getAll() {
        return ApiResponse.success(classifyService.getAllClassification());
    }

    @GetMapping("/by-route")
    @ApiOperation("根据路段节点对获取分类数据")
    public ApiResponse<List<Integer>> getByRoute(@RequestParam String oName, @RequestParam String dName) {
        try {
            return ApiResponse.success(classifyService.getClassificationByRoute(oName, dName));
        } catch (Exception e) {
            return ApiResponse.error("查询失败: " + e.getMessage());
        }
    }
    @GetMapping("/by-time")
    @ApiOperation("根据时间节点对获取所有路段分类数据 yyyy/mm/dd hh:mm:ss")
    @ApiParam(example = "2024/12/29 02:05")
    public ApiResponse<?> getBtTime(@RequestParam  String timeStr) {
        return ApiResponse.success(classifyService.getClassificationByTimeIndex(timeStr));
    }


}
