package com.xvpi.smarttransportbackend.controller;

import com.xvpi.smarttransportbackend.entity.ApiResponse;
import com.xvpi.smarttransportbackend.service.AICommandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/command")
@Api(tags = "指令信息")
public class AICommandController {

    @Autowired
    private AICommandService commandService;

    @GetMapping("/latest")
    @ApiOperation("获取未处理的指令")
    public ApiResponse<?> getLatest() {
        return ApiResponse.success(commandService.getUnprocessedCommands());
    }
    @GetMapping("/findById")
    @ApiOperation("通过指令ID获取指令")
    public ApiResponse<?> getCommandById(@RequestParam Long commandId) {
        return ApiResponse.success(commandService.getCommandById(commandId));
    }

    @GetMapping("/generate-command")
    @ApiOperation("生成指令")
    public ApiResponse<?> generateCommand(@RequestParam String predictTime) {
        commandService.generateAICommands(predictTime);
        return ApiResponse.success("已生成指令");
    }
    @GetMapping("/dispatchcommand")
    @ApiOperation("分配待分配的指令")
    public ApiResponse<?> dispatchCommand() {
        commandService.dispatchUnprocessedCommands();
        return ApiResponse.success("已分配待分配指令");
    }
    @GetMapping("/stats")
    @ApiOperation("获取当天总警情数和待处理警情数yyyy-mm-dd")
    public ApiResponse<Map<String, Integer>> getCommandStats(@RequestParam String date) {
        // 将前端传入的日期字符串转为 LocalDateTime
        LocalDate localDate = LocalDate.parse(date); // 例如 "2025-04-22"
        LocalDateTime dateTime = localDate.atStartOfDay();

        Map<String, Integer> stats = commandService.getCommandStatistics(dateTime);
        return ApiResponse.success(stats);
    }

}
