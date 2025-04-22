package com.xvpi.smarttransportbackend.service;

import com.xvpi.smarttransportbackend.entity.EmergencyReport;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public interface EmergencyReportService {
    void submit(String type, String description, MultipartFile photo, HttpServletRequest request) throws IOException;
    List<EmergencyReport> getMyReports(HttpServletRequest request);
}
