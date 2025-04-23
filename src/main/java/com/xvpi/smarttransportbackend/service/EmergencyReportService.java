package com.xvpi.smarttransportbackend.service;

import com.xvpi.smarttransportbackend.entity.EmergencyReport;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface EmergencyReportService {
    void submit(String type, String description, MultipartFile photo, HttpServletRequest request) throws IOException;
    List<EmergencyReport> getMyReports(HttpServletRequest request);
    List<EmergencyReport> getReports(Long officerId, String status, String type, LocalDateTime start, LocalDateTime end);
    Map<String, Integer> countByType();
    List<EmergencyReport> getHeatmapPoints();
    void exportToExcel(HttpServletResponse response,Long officerId,String status,String type,LocalDateTime start, LocalDateTime end)throws IOException;

    boolean respondToReport(Long reportId, String status, String response);
}
