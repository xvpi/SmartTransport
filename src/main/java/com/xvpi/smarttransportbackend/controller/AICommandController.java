package com.xvpi.smarttransportbackend.controller;

import com.xvpi.smarttransportbackend.entity.ApiResponse;
import com.xvpi.smarttransportbackend.service.AICommandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/mark-processed")
    @ApiOperation("标记指令已处理")
    public ApiResponse<?> markProcessed(@RequestParam Long id) {
        commandService.markAsProcessed(id);
        return ApiResponse.success("已标记为已处理");
    }

    @GetMapping("/generate-command")
    @ApiOperation("生成指令")
    public ApiResponse<?> generateCommand(@RequestParam String predictTime) {
        commandService.generateAICommands(predictTime);
        return ApiResponse.success("已生成指令");
    }
}
