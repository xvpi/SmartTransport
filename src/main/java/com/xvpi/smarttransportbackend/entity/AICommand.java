package com.xvpi.smarttransportbackend.entity;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "ai_command")
public class AICommand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String route;

    private String suggestion;

    @Column(name = "generate_time")
    private LocalDateTime generateTime;

    @Column(name = "severity_level")
    private Integer severityLevel;

    private Boolean processed;
}
