package com.xvpi.smarttransportbackend.service;

import com.xvpi.smarttransportbackend.entity.CameraDevice;

import java.util.List;
import java.util.Optional;

public interface CameraDeviceService {
    List<CameraDevice> getAllDevices();
    Optional<CameraDevice> getByShortName(String shortName);
}

