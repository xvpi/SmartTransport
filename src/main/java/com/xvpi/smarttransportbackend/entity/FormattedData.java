package com.xvpi.smarttransportbackend.entity;
import lombok.Data;

import javax.persistence.*;
@Data
@Entity
@Table(name = "formatted_data")
public class FormattedData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String roadName;
    private String roadNo;
    private String plateNo;
    private String laneNo;
    private String formattedTime;
}
