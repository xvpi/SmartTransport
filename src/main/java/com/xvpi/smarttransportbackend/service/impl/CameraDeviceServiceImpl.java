package com.xvpi.smarttransportbackend.service.impl;

import com.xvpi.smarttransportbackend.entity.CameraDevice;
import com.xvpi.smarttransportbackend.entity.TrafficOfficer;
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
    @Override
    public CameraDevice addCamera(CameraDevice cameraDevice){
        return cameraDeviceRepository.save(cameraDevice);
    }
    @Override
    public void deleteCamera(Integer id){
        cameraDeviceRepository.deleteById(id);
    }
    @Override
    public CameraDevice updateCamera(CameraDevice cameraDevice) {
        CameraDevice existing = cameraDeviceRepository.findById(cameraDevice.getId())
                .orElseThrow(() -> new RuntimeException("Camera not found"));
        existing.setName(cameraDevice.getName());
        existing.setGpsPoint(cameraDevice.getGpsPoint());
        existing.setStatus(cameraDevice.getStatus());
        existing.setType(cameraDevice.getType());
        existing.setShortName(cameraDevice.getShortName());
        existing.setStatus(cameraDevice.getStatus());
        existing.setArea(cameraDevice.getArea());
        existing.setCreateTime(cameraDevice.getCreateTime());
        return cameraDeviceRepository.save(existing);
    }

}
