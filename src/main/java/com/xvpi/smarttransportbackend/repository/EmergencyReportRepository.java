package com.xvpi.smarttransportbackend.repository;

import com.xvpi.smarttransportbackend.entity.EmergencyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyReportRepository extends JpaRepository<EmergencyReport, Long> {
    List<EmergencyReport> findByOfficerId(Long officerId);
}
