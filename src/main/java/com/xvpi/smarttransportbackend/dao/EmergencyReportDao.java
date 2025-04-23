package com.xvpi.smarttransportbackend.dao;

import com.xvpi.smarttransportbackend.entity.EmergencyReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.*;

@Repository
public class EmergencyReportDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<EmergencyReport> getReports(Long officerId, String status, String type, LocalDateTime start, LocalDateTime end) {
        StringBuilder sql = new StringBuilder("SELECT * FROM emergency_report WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (officerId != null) {
            sql.append(" AND officer_id = ?");
            params.add(officerId);
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND status = ?");
            params.add(status);
        }
        if (type != null && !type.isEmpty()) {
            sql.append(" AND type = ?");
            params.add(type);
        }
        if (start != null) {
            sql.append(" AND report_time >= ?");
            params.add(Timestamp.valueOf(start));
        }
        if (end != null) {
            sql.append(" AND report_time <= ?");
            params.add(Timestamp.valueOf(end));
        }

        return jdbcTemplate.query(sql.toString(), params.toArray(), new BeanPropertyRowMapper<>(EmergencyReport.class));
    }


    public Map<String, Integer> countByType() {
        String sql = "SELECT type, COUNT(*) as count FROM emergency_report GROUP BY type";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        Map<String, Integer> result = new HashMap<>();
        for (Map<String, Object> row : list) {
            result.put((String) row.get("type"), ((Long) row.get("count")).intValue());
        }
        return result;
    }


    public List<EmergencyReport> getHeatmapPoints() {
        String sql = "SELECT location_lat, location_lng FROM emergency_report";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(EmergencyReport.class));
    }
    public List<EmergencyReport> getRawLatLngList() {
        String sql = "SELECT location_lat, location_lng FROM emergency_report";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            EmergencyReport report = new EmergencyReport();
            report.setLocationLat(rs.getDouble("location_lat"));
            report.setLocationLng(rs.getDouble("location_lng"));
            return report;
        });
    }
}
