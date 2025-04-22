package com.xvpi.smarttransportbackend.service.impl;

import com.xvpi.smarttransportbackend.config.FileUploadUtil;
import com.xvpi.smarttransportbackend.entity.EmergencyReport;
import com.xvpi.smarttransportbackend.entity.TrafficOfficer;
import com.xvpi.smarttransportbackend.repository.EmergencyReportRepository;
import com.xvpi.smarttransportbackend.service.EmergencyReportService;
import com.xvpi.smarttransportbackend.service.PredictionService;
import com.xvpi.smarttransportbackend.service.TrafficOfficerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EmergencyReportServiceImpl implements EmergencyReportService {
    @Autowired
    private EmergencyReportRepository reportRepository;

    @Autowired
    private TrafficOfficerService officerService;
    @Autowired
    private FileUploadUtil fileUploadUtil;
    @Override
    public void submit(String type, String description, MultipartFile photo, HttpServletRequest request) throws IOException {
        TrafficOfficer officer = officerService.getCurrentOfficer(request);
            String photoUrl = fileUploadUtil.saveFile(photo, "emergency_photos");
            EmergencyReport report = new EmergencyReport();
            report.setOfficerId(officer.getId());
            report.setDescription(description);
            report.setType(type);
            report.setStatus("待处理");
            report.setLocationLat(officer.getCurrentLat());
            report.setLocationLng(officer.getCurrentLng());
            report.setPhotoUrl(photoUrl);
            report.setReportTime(LocalDateTime.now());
        reportRepository.save(report);
    }



    @Override
    public List<EmergencyReport> getMyReports(HttpServletRequest request) {
        TrafficOfficer officer = officerService.getCurrentOfficer(request);
        return reportRepository.findByOfficerId(officer.getId());
    }
}
