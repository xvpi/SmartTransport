package com.xvpi.smarttransportbackend.service.impl;

import com.alibaba.excel.EasyExcel;
import com.xvpi.smarttransportbackend.config.FileUploadUtil;
import com.xvpi.smarttransportbackend.dao.EmergencyReportDao;
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
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmergencyReportServiceImpl implements EmergencyReportService {
    @Autowired
    private EmergencyReportRepository reportRepository;
    @Autowired
    private EmergencyReportDao reportDao;
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
   public void exportToExcel(HttpServletResponse response, Long officerId, String status, String type,
                           LocalDateTime start,
                       LocalDateTime end)throws IOException{
        List<EmergencyReport> reports = getReports(officerId, status, type, start, end);

        // 转为 List<List<Object>>，保持列顺序
        List<List<Object>> dataRows = reports.stream().map(report -> Arrays.asList(
                (Object) report.getId(),
                (Object) report.getOfficerId(),
                (Object) (report.getReportTime() != null ? report.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : ""),
                (Object) report.getLocationLat(),
                (Object) report.getLocationLng(),
                (Object) report.getType(),
                (Object) report.getDescription(),
                (Object) report.getStatus(),
                (Object) report.getResponse(),
                (Object) report.getPhotoUrl()
        )).collect(Collectors.toList());

        // 设置响应头
        response.setContentType("application/vnd.ms-excel");
        String filename = "emergency_reports_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8));

        // 写入 Excel
        EasyExcel.write(response.getOutputStream())
                .head(excelHead()) // 手动定义列头
                .sheet("事故报告")
                .doWrite(dataRows);

    }
    private List<List<String>> excelHead() {
        return Arrays.asList(
                Collections.singletonList("ID"),
                Collections.singletonList("交警ID"),
                Collections.singletonList("上报时间"),
                Collections.singletonList("纬度"),
                Collections.singletonList("经度"),
                Collections.singletonList("类型"),
                Collections.singletonList("描述"),
                Collections.singletonList("状态"),
                Collections.singletonList("响应信息"),
                Collections.singletonList("图片URL")
        );
    }



    @Override
    public List<EmergencyReport> getMyReports(HttpServletRequest request) {
        TrafficOfficer officer = officerService.getCurrentOfficer(request);
        return reportRepository.findByOfficerId(officer.getId());
    }
    @Override
    public List<EmergencyReport> getReports(Long officerId, String status, String type, LocalDateTime start, LocalDateTime end) {
        return reportDao.getReports(officerId,status,type,start,end);
    }
    @Override
    public Map<String, Integer> countByType() {
        return reportDao.countByType();
    }

    @Override
    public List<EmergencyReport> getHeatmapPoints() {
        return reportDao.getRawLatLngList();
    }
    @Override
    public boolean respondToReport(Long reportId, String status, String response){
        Optional<EmergencyReport> optional = reportRepository.findById(reportId);
        if (optional.isPresent()) {
            EmergencyReport report = optional.get();
            report.setStatus(status);
            report.setResponse(response);
            reportRepository.save(report);
            return true;
        }
        return false;
    }
}
