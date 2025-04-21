// src/main/java/com/xvpi/smarttransportbackend/controller/PredictController.java

package com.xvpi.smarttransportbackend.controller;

import com.xvpi.smarttransportbackend.config.PythonRunner;
import com.xvpi.smarttransportbackend.entity.ApiResponse;
import com.xvpi.smarttransportbackend.service.PredictionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/predict")
@Api(tags = "预测信息")
public class PredictController {

    @Autowired
    private PredictionService predictionService;

    @GetMapping("/all")
    @ApiOperation("获取全部预测数据")
    public ApiResponse<List<List<Integer>>> getAll(@RequestParam String predictTime) {
        return ApiResponse.success(predictionService.getAllPredictions(predictTime,"flow"));
    }
    @GetMapping("/all-speed")
    @ApiOperation("获取全部预测数据-速度")
    public ApiResponse<List<List<Integer>>> getAllSpeed(@RequestParam String predictTime) {
        return ApiResponse.success(predictionService.getAllPredictions(predictTime,"speed"));
    }

    @GetMapping("/by-route")
    @ApiOperation("根据路段节点对获取预测数据")
    public ApiResponse<List<Integer>> getByRoute(@RequestParam String oName, @RequestParam String dName,@RequestParam String predictTime) {
        try {
            return ApiResponse.success(predictionService.getPredictionByRoute(oName, dName,predictTime));
        } catch (Exception e) {
            return ApiResponse.error("查询失败: " + e.getMessage());
        }
    }
}

