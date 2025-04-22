package com.xvpi.smarttransportbackend.repository;

import com.xvpi.smarttransportbackend.entity.TrafficOfficer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrafficOfficerRepository extends JpaRepository<TrafficOfficer, Long> {
    Optional<TrafficOfficer> findByUsername(String username);
    List<TrafficOfficer> findByStatus(Integer status);

    List<TrafficOfficer> findAllByOrderByIdAsc();
}
