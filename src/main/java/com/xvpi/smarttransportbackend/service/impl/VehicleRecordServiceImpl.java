package com.xvpi.smarttransportbackend.service.impl;

import com.xvpi.smarttransportbackend.dao.VehicleRecordDao;
import com.xvpi.smarttransportbackend.entity.RoadSection;
import com.xvpi.smarttransportbackend.entity.VehicleRecord;
import com.xvpi.smarttransportbackend.repository.RoadSectionDao;
import com.xvpi.smarttransportbackend.repository.VehicleRecordRepository;
import com.xvpi.smarttransportbackend.service.VehicleRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VehicleRecordServiceImpl implements VehicleRecordService {
    @Autowired
    private VehicleRecordDao vehicleRecordDao;
    @Override
    public Map<String, Long> getVehicleTypeCount(LocalDateTime start, LocalDateTime end) {
        return vehicleRecordDao.countVehicleTypesByTimeRange(start, end);
    }
//    private final VehicleRecordRepository vehicleRecordRepository;
//
//    @Override
//    public Map<String, Long> getVehicleCountByRoadLastHour() {
//        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
//        List<VehicleRecord> records = vehicleRecordRepository.findByPassTimeAfter(oneHourAgo);
//
//        return records.stream()
//                .collect(Collectors.groupingBy(VehicleRecord::getRoadName, Collectors.counting()));
//    }
//
//    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//    @Override
//    public List<Map<String, Object>> getVehicleTypeCountInLastTenMinutes(String timeStr) {
//        LocalDateTime endTime = LocalDateTime.parse(timeStr, FORMATTER);
//        LocalDateTime startTime = endTime.minusMinutes(10);
//
//        List<Object[]> rawResult = vehicleRecordRepository.countVehicleTypesBetween(startTime, endTime);
//
//        List<Map<String, Object>> result = new ArrayList<>();
//        for (Object[] row : rawResult) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("vehicleType", row[0]);
//            map.put("count", row[1]);
//            result.add(map);
//        }
//        return result;
//    }
}
