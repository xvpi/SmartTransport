package com.xvpi.smarttransportbackend.service.impl;

import com.xvpi.smarttransportbackend.entity.CameraDevice;
import com.xvpi.smarttransportbackend.repository.CameraDeviceRepository;
import com.xvpi.smarttransportbackend.service.CameraDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CameraDeviceServiceImpl implements CameraDeviceService {
    @Autowired
    private final CameraDeviceRepository cameraDeviceRepository;

    public CameraDeviceServiceImpl(CameraDeviceRepository cameraDeviceRepository) {
        this.cameraDeviceRepository = cameraDeviceRepository;
    }

    @Override
    public List<CameraDevice> getAllDevices() {
        return cameraDeviceRepository.findAll();
    }

    @Override
    public Optional<CameraDevice> getByShortName(String shortName) {
        return cameraDeviceRepository.findByShortName(shortName);
    }
}
