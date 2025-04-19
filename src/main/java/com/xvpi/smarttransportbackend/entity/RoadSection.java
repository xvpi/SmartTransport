package com.xvpi.smarttransportbackend.entity;

public class RoadSection {
    private Integer id;
    private String oName;
    private String dName;
    private String oGis;
    private String dGis;
    private Float distance;
    private String pathGis;
    private Byte isKeyNode;

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

    public Float getDistance() { return distance; }
    public void setDistance(Float distance) { this.distance = distance; }

    public String getPathGis() { return pathGis; }
    public void setPathGis(String pathGis) { this.pathGis = pathGis; }

    public Byte getIsKeyNode() { return isKeyNode; }
    public void setIsKeyNode(Byte isKeyNode) { this.isKeyNode = isKeyNode; }
}
