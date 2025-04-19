package com.xvpi.smarttransportbackend.service.impl;

import com.xvpi.smarttransportbackend.config.PythonRunner;
import com.xvpi.smarttransportbackend.service.PredictionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PredictionServiceImpl implements PredictionService {

    private static final Logger logger = LoggerFactory.getLogger(PredictionServiceImpl.class);
    private static final String PYTHON_SCRIPT_PATH = "python/predict_model/Predict.py";

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
        public List<List<Integer>> getAllPredictions() {
            List<String> output;
            try {
                output = PythonRunner.runPythonScript(PYTHON_SCRIPT_PATH);
            } catch (Exception e) {
                throw new RuntimeException("执行Python脚本失败", e);
            }

            List<Integer> flatList = new ArrayList<>();
            for (String line : output) {
                if (line != null && !line.trim().isEmpty()) {
                    line = line.replaceAll("[\\[\\]]", "").trim();
                    String[] nums = line.split(",");
                    for (String num : nums) {
                        try {
                            flatList.add(Integer.parseInt(num.trim()));
                        } catch (NumberFormatException e) {
                            logger.warn("无效数字格式 [{}]，跳过", num);
                        }
                    }
                }
            }

            List<List<Integer>> result = new ArrayList<>();
            for (int i = 0; i + 2 < flatList.size(); i += 3) {
                result.add(List.of(flatList.get(i), flatList.get(i + 1), flatList.get(i + 2)));
            }

            return result;
        }

    @Override
    public List<Integer> getPredictionByRoute(String oName, String dName) {
        String key = oName + "-" + dName;
        int index = ROUTE_NAMES.indexOf(key);
        if (index == -1) {
            logger.warn("请求的路段 [{}] 不存在于定义列表中", key);
            throw new IllegalArgumentException("未找到对应路段: " + key);
        }

        List<List<Integer>> allPredictions = getAllPredictions();
        if (index >= allPredictions.size()) {
            logger.error("预测结果数量不足，索引 [{}] 超出范围", index);
            throw new IndexOutOfBoundsException("预测结果索引超出范围");
        }

        return allPredictions.get(index);
    }

}
