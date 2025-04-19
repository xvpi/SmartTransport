package com.xvpi.smarttransportbackend.entity;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "traffic_suggestion")
public class TrafficSuggestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime suggestionTime;
    private String targetRoad;
    @Column(columnDefinition = "TEXT")
    private String reason;
    @Column(columnDefinition = "TEXT")
    private String recommendation;
    private String status;
    private String assignedTo;
    private LocalDateTime processedTime;
}
