package com.xvpi.smarttransportbackend.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "traffic_task")
public class TrafficTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String route;
    private String taskType;
    private Integer severityLevel;
    private Integer officerCount;
    private Integer status;
    private LocalDateTime assignedTime;
    private LocalDateTime completedTime;
    private String suggestion;
}


