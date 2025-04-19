package com.xvpi.smarttransportbackend.service.impl;

import com.xvpi.smarttransportbackend.entity.VehicleRecord;
import com.xvpi.smarttransportbackend.repository.VehicleRecordRepository;
import com.xvpi.smarttransportbackend.service.VehicleRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleRecordServiceImpl implements VehicleRecordService {

    private final VehicleRecordRepository repository;

    @Override
    public Map<String, Long> getVehicleCountByRoadLastHour() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        List<VehicleRecord> records = repository.findByPassTimeAfter(oneHourAgo);

        return records.stream()
                .collect(Collectors.groupingBy(VehicleRecord::getRoadName, Collectors.counting()));
    }
}
