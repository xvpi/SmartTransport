package com.xvpi.smarttransportbackend.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "officer_task_relation")
public class OfficerTaskRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long officerId;
    private Long taskId;
}
