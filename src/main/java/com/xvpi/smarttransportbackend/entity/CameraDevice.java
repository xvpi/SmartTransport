package com.xvpi.smarttransportbackend.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "camera_device")
public class CameraDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String gpsPoint;
    private String type;
    private String area;
    private String shortName;
    private Byte status;
    private LocalDateTime createTime;
}

