package com.xvpi.smarttransportbackend.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xvpi.smarttransportbackend.config.PythonRunner;
import com.xvpi.smarttransportbackend.service.ClassifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClassifyServiceImpl implements ClassifyService {

    private static final Logger logger = LoggerFactory.getLogger(PredictionServiceImpl.class);
    private static final String PYTHON_SCRIPT_PATH = "python/classify_model/classify.py";
    private static final String RESULT_PATH = "python/classify_model/result.npy";
    private static final LocalDateTime BASE_TIME = LocalDateTime.of(2024, 12, 29, 0, 0);

    private static final List<String> ROUTE_NAMES = List.of(
            "红11-红12", "红12-红4", "红13-红11", "红13-蓝4", "红1-红4", "红2-蓝5", "红4-红5", "红5-红13", "红8-红9", "红9-蓝2",
            "蓝10-绿14", "蓝10-绿6", "蓝11-蓝13", "蓝11-蓝26", "蓝11-绿7", "蓝12-蓝17", "蓝13-红2", "蓝13-红3", "蓝13-红8", "蓝13-蓝17",
            "蓝13-蓝20", "蓝14-蓝4", "蓝15-红4", "蓝15-红7", "蓝15-蓝10", "蓝15-蓝6", "蓝16-红4", "蓝16-红7", "蓝16-蓝10", "蓝16-蓝15",
            "蓝17-蓝13", "蓝17-蓝26", "蓝18-蓝4", "蓝19-蓝4", "蓝1-蓝16", "蓝20-蓝12", "蓝20-蓝16", "蓝21-红2", "蓝21-红3", "蓝21-红8",
            "蓝21-蓝20", "蓝22-蓝6", "蓝23-红1", "蓝25-红2", "蓝25-红3", "蓝25-红8", "蓝2-蓝1", "蓝2-蓝7", "蓝3-蓝17", "蓝4-蓝15",
            "蓝4-绿14", "蓝4-绿6", "蓝5-蓝1", "蓝5-蓝7", "蓝6-蓝12", "蓝6-蓝16", "蓝6-蓝21", "蓝7-蓝12", "蓝7-蓝21", "蓝8-蓝15",
            "蓝8-绿14", "蓝8-绿6", "蓝9-蓝15", "蓝9-绿6", "绿10-绿15", "绿11-绿15", "绿11-绿4", "绿13-蓝9", "绿13-绿17", "绿14-蓝9",
            "绿14-绿17", "绿15-绿13", "绿15-绿3", "绿16-绿13", "绿16-绿8", "绿17-绿3", "绿17-绿8", "绿3-蓝8", "绿4-蓝8", "绿4-绿16",
            "绿5-蓝8", "绿6-绿1", "绿6-绿11", "绿7-蓝8", "绿7-绿11", "绿7-绿16", "绿8-绿4", "绿9-绿15", "绿9-绿4"
    );
    @Override
    public List<List<Integer>> getAllClassification() {
        List<String> output;
        try {
            output = PythonRunner.runPythonScript(PYTHON_SCRIPT_PATH);
        } catch (Exception e) {
            throw new RuntimeException("执行 classify.py 失败", e);
        }

        StringBuilder sb = new StringBuilder();
        for (String line : output) {
            sb.append(line);
        }

        // 解析 JSON 到 Java List<List<Integer>>
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(sb.toString(), new TypeReference<>() {});
        } catch (IOException e) {
            throw new RuntimeException("解析 classify.py 输出失败", e);
        }

    }

    @Override
    public List<Integer> getClassificationByRoute(String oName, String dName) {
        String key = oName + "-" + dName;
        int index = ROUTE_NAMES.indexOf(key);
        if (index == -1) {
            logger.warn("请求的路段 [{}] 不存在于定义列表中", key);
            throw new IllegalArgumentException("未找到对应路段: " + key);
        }
        List<List<Integer>> allPredictions = getAllClassification();
        if (index >= allPredictions.size()) {
            logger.error("预测结果数量不足，索引 [{}] 超出范围", index);
            throw new IndexOutOfBoundsException("预测结果索引超出范围");
        }

        return allPredictions.get(index);
    }
    private int getIndexFromTime(String timeStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(timeStr, formatter);
            int hour = dateTime.getHour();
            int minute = dateTime.getMinute();
            return hour * 6 + (minute / 10);
        } catch (Exception e) {
            logger.warn("时间格式错误: {}，应为 yyyy/MM/dd HH:mm", timeStr);
            return -1;
        }
    }

    @Override
    public Map<String, Object> getClassificationByTimeIndex(String timeStr) {
        int timeIndex = getIndexFromTime(timeStr);
// 读取 result.npy
        INDArray resultArray;
        resultArray = Nd4j.createFromNpyFile(new File(RESULT_PATH));

        int totalTimeSteps = resultArray.columns();
        int totalRoutes = resultArray.rows();

        if (timeIndex < 0 || timeIndex >= totalTimeSteps) {
            throw new IllegalArgumentException("时间索引越界：有效范围是 0 到 " + (totalTimeSteps - 1));
        }


        // 生成时间字符串
        LocalDateTime startTime = BASE_TIME.plusMinutes(timeIndex * 10L);
        LocalDateTime endTime = startTime.plusMinutes(10);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        String timeRange = formatter.format(startTime) + "-" + formatter.format(endTime);

        // 构建数据列表
        List<Map<String, Object>> stateList = new ArrayList<>();
        for (int i = 0; i < totalRoutes; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("route", ROUTE_NAMES.get(i));
            item.put("state", resultArray.getInt(i, timeIndex));
            stateList.add(item);
        }

        // 返回数据
        Map<String, Object> result = new HashMap<>();
        result.put("time", timeRange);
        result.put("data", stateList);
        return result;
    }

}
