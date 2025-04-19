package com.xvpi.smarttransportbackend.entity;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "congestion_forecast")
public class CongestionForecast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime forecastTime;
    private String roadNo;
    private String roadName;
    private Integer forecastPeriodMinutes;
    private Integer congestionLevel;
    private Integer vehicleCount;
    private Float averageSpeed;
    @Column(columnDefinition = "TEXT")
    private String suggestion;
    private LocalDateTime createTime;
}

