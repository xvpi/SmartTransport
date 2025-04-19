package com.xvpi.smarttransportbackend.service;

import java.util.Map;

public interface VehicleRecordService {
    Map<String, Long> getVehicleCountByRoadLastHour();
}
