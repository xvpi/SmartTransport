package com.xvpi.smarttransportbackend.dao;
import com.xvpi.smarttransportbackend.entity.VehicleRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.*;
import java.util.*;
@Repository
public class VehicleRecordDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取全部车辆记录
     */
    public List<VehicleRecord> getAll() {
        String sql = "SELECT * FROM vehicle_record";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(VehicleRecord.class));
    }

    /**
     * 根据时间范围统计每种车型的数量
     */
    public Map<String, Long> countVehicleTypesByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        String sql = "SELECT vehicle_type, COUNT(*) AS total " +
                "FROM vehicle_record " +
                "WHERE pass_time BETWEEN ? AND ? " +
                "GROUP BY vehicle_type";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, Timestamp.valueOf(startTime), Timestamp.valueOf(endTime));
        Map<String, Long> result = new HashMap<>();

        for (Map<String, Object> row : rows) {
            String type = (String) row.get("vehicle_type");
            Long count = ((Number) row.get("total")).longValue();
            result.put(type, count);
        }

        return result;
    }

    /**
     * 查询某一时间段的所有车辆记录
     */
    public List<VehicleRecord> findByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        String sql = "SELECT * FROM vehicle_record WHERE pass_time BETWEEN ? AND ?";
        return jdbcTemplate.query(
                sql,
                new Object[]{Timestamp.valueOf(startTime), Timestamp.valueOf(endTime)},
                new BeanPropertyRowMapper<>(VehicleRecord.class)
        );
    }
}
