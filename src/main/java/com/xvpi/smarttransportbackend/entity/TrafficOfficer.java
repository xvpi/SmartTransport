package com.xvpi.smarttransportbackend.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "traffic_officer")
public class TrafficOfficer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(nullable = false, unique = true)
    private String username;
    private String password;
    private String phone;
    @Column(name = "status")
    private Integer status;
    private Double currentLat;
    private Double currentLng;
    private LocalDateTime lastUpdateTime;
}
