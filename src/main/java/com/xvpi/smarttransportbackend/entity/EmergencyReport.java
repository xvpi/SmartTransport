package com.xvpi.smarttransportbackend.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "emergency_report")
public class EmergencyReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long officerId;
    private LocalDateTime reportTime;

    private Double locationLat;
    private Double locationLng;

    private String type;          // 事件类型
    private String description;   // 描述
    private String status;        // 状态
    private String response;      // 可选：处理反馈
    private String photoUrl; // 图片地址
}

