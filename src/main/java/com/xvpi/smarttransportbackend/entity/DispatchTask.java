package com.xvpi.smarttransportbackend.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "dispatch_task")
public class DispatchTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long officerId;
    private Long commandId;
    private LocalDateTime assignTime;
    private Integer status;

}
