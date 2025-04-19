package com.xvpi.smarttransportbackend.repository;

import com.xvpi.smarttransportbackend.entity.CameraDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CameraDeviceRepository extends JpaRepository<CameraDevice, Integer> {
    Optional<CameraDevice> findByShortName(String shortName);
}

