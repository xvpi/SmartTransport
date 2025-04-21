package com.xvpi.smarttransportbackend.entity;

import io.swagger.models.auth.In;

public class RoadSection {
    private Integer id;
    private String oName;
    private String dName;
    private String oGis;
    private String dGis;
    private String distance;
    private String pathGis;
    private Integer roadCapacity;
    // Getter and Setter 方法
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getOName() { return oName; }
    public void setOName(String oName) { this.oName = oName; }

    public String getDName() { return dName; }
    public void setDName(String dName) { this.dName = dName; }

    public String getOGis() { return oGis; }
    public void setOGis(String oGis) { this.oGis = oGis; }

    public String getDGis() { return dGis; }
    public void setDGis(String dGis) { this.dGis = dGis; }

    public String getDistance() { return distance; }
    public void setDistance(String distance) { this.distance = distance; }

    public String getPathGis() { return pathGis; }
    public void setPathGis(String pathGis) { this.pathGis = pathGis; }

    public Integer getRoadCapacity() { return roadCapacity; }
    public void setRoadCapacity(Integer roadCapacity) { this.roadCapacity = roadCapacity; }
}
