package com.xvpi.smarttransportbackend.repository;
import com.xvpi.smarttransportbackend.entity.CongestionForecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CongestionForecastRepository extends JpaRepository<CongestionForecast, Integer> {}

