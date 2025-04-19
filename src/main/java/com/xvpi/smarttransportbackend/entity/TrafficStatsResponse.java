package com.xvpi.smarttransportbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
public class TrafficStatsResponse {
    private List<String> timeSlots;          // ["00:00-00:10", ...]
    private List<String> roadPairs;          // ["红11-红12", ...]
    private Map<String, List<Integer>> data; // key: 路段对，value: 每个时段的数量

}
