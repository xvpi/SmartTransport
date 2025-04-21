package com.xvpi.smarttransportbackend.service.impl;

import com.xvpi.smarttransportbackend.entity.AICommand;
import com.xvpi.smarttransportbackend.entity.RoadSection;
import com.xvpi.smarttransportbackend.repository.AICommandRepository;
import com.xvpi.smarttransportbackend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AICommandServiceImpl implements AICommandService {
    @Autowired
    private AICommandRepository commandRepository;

    @Autowired
    private ClassifyService classifyService;

    @Autowired
    private PredictionService predictService;

    @Autowired
    private RoadSectionService roadSectionService;

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
    public void generateAICommands(String currentTimeStr) {
        Map<String, Object> classificationResult = classifyService.getClassificationByTimeIndex(currentTimeStr);
        List<List<Integer>> predictedTraffic = predictService.getAllPredictions(currentTimeStr,"speed");

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> data = (List<Map<String, Object>>) classificationResult.get("data");

        // 第一步：将预测数据与 ROUTE_NAMES 组合成 Map<String, List<Integer>>
        Map<String, List<Integer>> routePredictionMap = new HashMap<>();
        for (int i = 0; i < ROUTE_NAMES.size(); i++) {
            if (i < predictedTraffic.size()) {
                routePredictionMap.put(ROUTE_NAMES.get(i), predictedTraffic.get(i));
            }
        }

        for (Map<String, Object> segment : data) {
            String route = (String) segment.get("route");
            int state = (Integer) segment.get("state"); // 0正常，1拥堵，2严重拥堵

            List<Integer> future = routePredictionMap.getOrDefault(route, Arrays.asList(0, 0, 0));
            double avgFutureTraffic = future.stream().mapToInt(Integer::intValue).average().orElse(0.0);

            if (state == 2 && avgFutureTraffic < 45) {
                // 拆分起点和终点
                String[] names = route.split("-");
                if (names.length != 2) continue;

                String oName = names[0];
                String dName = names[1];

                RoadSection section = roadSectionService.getByOAndDName(oName, dName);
                int roadCapacity = section != null ? section.getRoadCapacity() : 0;

                int officerCount = 1;
                if (roadCapacity > 400) {
                    officerCount = 3;
                } else if (roadCapacity >= 200) {
                    officerCount = 2;
                }

                String suggestion = String.format(
                        "【AI建议】%s 路段严重拥堵，建议派出 %d 名交警或调整信号配时。",
                        route, officerCount
                );

                AICommand cmd = new AICommand();
                cmd.setRoute(route);
                cmd.setGenerateTime(LocalDateTime.now());
                cmd.setSeverityLevel(state);
                cmd.setProcessed(false);
                cmd.setSuggestion(suggestion);

                commandRepository.save(cmd);
            }
        }
    }

    @Override
    public List<AICommand> getUnprocessedCommands() {
        return commandRepository.findByProcessedFalse();
    }
    @Override
    public void markAsProcessed(Long id) {
        commandRepository.findById(id).ifPresent(cmd -> {
            cmd.setProcessed(true);
            commandRepository.save(cmd);
        });
    }
}
