package com.xvpi.smarttransportbackend.config; //Haversine 算法
public class GeoUtils {
    public static double calcDistance(double lat1, double lng1, double lat2, double lng2) {
        final double R = 6371e3;
        double phi1 = Math.toRadians(lat1);
        double phi2 = Math.toRadians(lat2);
        double dPhi = Math.toRadians(lat2 - lat1);
        double dLambda = Math.toRadians(lng2 - lng1);

        double a = Math.sin(dPhi/2) * Math.sin(dPhi/2) +
                Math.cos(phi1) * Math.cos(phi2) *
                        Math.sin(dLambda/2) * Math.sin(dLambda/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return R * c; // 距离（米）
    }
}

