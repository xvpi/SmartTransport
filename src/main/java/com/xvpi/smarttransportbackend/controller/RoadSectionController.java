package com.xvpi.smarttransportbackend.controller;

import com.xvpi.smarttransportbackend.entity.RoadSection;
import com.xvpi.smarttransportbackend.service.RoadSectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/api/road-section")
@Api(tags = "路段信息")
public class RoadSectionController {

    @Autowired
    private RoadSectionService roadSectionService;

    @GetMapping("/all")
    @ApiOperation("获取所有路段数据")
    public List<RoadSection> getAll() {
        return roadSectionService.getAll();
    }

    @GetMapping("/query")
    @ApiOperation("通过首尾节点查询路段")
    public RoadSection getByOAndD(@RequestParam String oName, @RequestParam String dName) {
        return roadSectionService.getByOAndDName(oName, dName);
    }
}
