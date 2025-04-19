package com.xvpi.smarttransportbackend.entity;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "vehicle_record")
public class VehicleRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "road_name")
    private String roadName;

    @Column(name = "pass_time")
    private LocalDateTime passTime;

    @Column(name = "road_no")
    private String roadNo;

    @Column(name = "plate_no")
    private String plateNo;

    @Column(name = "channel_name")
    private String channelName;

    @Column(name = "lane_no")
    private Integer laneNo;

    @Column(name = "vehicle_speed")
    private Float vehicleSpeed;

    @Column(name = "plate_color")
    private String plateColor;

    @Column(name = "vehicle_type")
    private String vehicleType;

    @Column(name = "create_time")
    private LocalDateTime createTime;

}
