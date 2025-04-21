package com.xvpi.smarttransportbackend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface VehicleRecordService {
    //Map<String, Long> getVehicleCountByRoadLastHour();
    Map<String, Long> getVehicleTypeCount(LocalDateTime start, LocalDateTime end);
}
