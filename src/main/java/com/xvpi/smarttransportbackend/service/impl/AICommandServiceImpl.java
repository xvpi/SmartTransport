package com.xvpi.smarttransportbackend.service.impl;

import com.xvpi.smarttransportbackend.dao.CommandDao;
import com.xvpi.smarttransportbackend.entity.AICommand;
import com.xvpi.smarttransportbackend.entity.RoadSection;
import com.xvpi.smarttransportbackend.repository.AICommandRepository;
import com.xvpi.smarttransportbackend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AICommandServiceImpl implements AICommandService {
    @Autowired
    private AICommandRepository commandRepository;
    @Autowired
    private TrafficOfficerService trafficOfficerService;
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
        // 将时间字符串解析为 LocalDateTime
        LocalDateTime generateTime = LocalDateTime.parse(currentTimeStr, DateTimeFormatter.ofPattern("yyyy/MM/dd " +
                "HH:mm:ss"));

        Map<String, Object> classificationResult = classifyService.getClassificationByTimeIndex(currentTimeStr);
        List<List<Integer>> predictedTraffic = predictService.getAllPredictions(currentTimeStr, "speed");

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> data = (List<Map<String, Object>>) classificationResult.get("data");

        Map<String, List<Integer>> routePredictionMap = new HashMap<>();
        for (int i = 0; i < ROUTE_NAMES.size(); i++) {
            if (i < predictedTraffic.size()) {
                routePredictionMap.put(ROUTE_NAMES.get(i), predictedTraffic.get(i));
            }
        }

        for (Map<String, Object> segment : data) {
            String route = (String) segment.get("route");
            int state = (Integer) segment.get("state");

            List<Integer> future = routePredictionMap.getOrDefault(route, Arrays.asList(0, 0, 0));
            double avgFutureTraffic = future.stream().mapToInt(Integer::intValue).average().orElse(0.0);

            if (state == 2 && avgFutureTraffic < 10) {
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
                        "【AI建议】%s 路段在 %s 出现严重拥堵，建议派出 %d 名交警或调整信号配时。",
                        route,
                        generateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        officerCount
                );
                AICommand cmd = new AICommand();
                cmd.setOfficerCount(officerCount);
                cmd.setStatus(false);
                cmd.setRoute(route);
                cmd.setGenerateTime(generateTime); // ✅ 使用解析后的时间
                cmd.setSeverityLevel(state);
                cmd.setProcessed(0);
                cmd.setSuggestion(suggestion);

                commandRepository.save(cmd);
            }
        }
        dispatchUnprocessedCommands();
    }


    @Override
    public List<AICommand> getUnprocessedCommands() {
        return commandRepository.findByStatus(false);}
    @Override
    public Optional<AICommand> getCommandById(Long commandId) {
        return commandRepository.findById(commandId);}
    @Autowired
    private CommandDao commandDao;
    @Override
    public Map<String, Integer> getCommandStatistics(LocalDateTime getTime) {
        int total = commandDao.countAllCommands(getTime);
        int unfinished = commandDao.countUnfinishedCommands(getTime);
        return Map.of(
                "total", total,
                "unprocessed", unfinished
        );
    }
    @Override
    public void dispatchUnprocessedCommands() {
        List<AICommand> cmds = getUnprocessedCommands();

        for (AICommand cmd : cmds) {
            int needAssign = cmd.getOfficerCount() - cmd.getProcessed();
            for (int i = 0; i < needAssign; i++) {
                try {
                    boolean result = trafficOfficerService.assignTask(cmd.getId());
                    if (!result) break;
                } catch (RuntimeException e) {
                    // 没有空闲交警或其他异常，提前退出本条指令
                    break;
                }
            }
        }
    }
}
