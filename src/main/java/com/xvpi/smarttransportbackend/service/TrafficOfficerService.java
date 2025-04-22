package com.xvpi.smarttransportbackend.service;

import com.xvpi.smarttransportbackend.entity.DispatchTask;
import com.xvpi.smarttransportbackend.entity.TrafficOfficer;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface TrafficOfficerService {
    TrafficOfficer register(TrafficOfficer officer);
    String login(String username, String rawPassword);
    TrafficOfficer getCurrentOfficer(HttpServletRequest request);
    void updatePositionById(Long id, Double currentLat, Double currentLng);
    boolean assignTask(Long commandId);
    void acceptTask(Long taskId);
    void cancelTask(Long taskId);
    List<DispatchTask> getCurrentTasks(Long officerId);
    TrafficOfficer addOfficer(TrafficOfficer officer);
    TrafficOfficer updateOfficer(TrafficOfficer officer);
    void deleteOfficer(Long id);
    void resetPassword(Long id, String newPassword);
    List<TrafficOfficer> getAllOfficers();
}
