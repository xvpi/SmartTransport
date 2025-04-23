package com.xvpi.smarttransportbackend.dao;

import com.xvpi.smarttransportbackend.entity.DispatchTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DispatchTaskDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    public List<DispatchTask> findByCondition(Long officerId, LocalDateTime start, LocalDateTime end, Integer status) {
        StringBuilder sql = new StringBuilder("SELECT * FROM dispatch_task WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (officerId != null) {
            sql.append(" AND officer_id = ?");
            params.add(officerId);
        }
        if (status != null) {
            sql.append(" AND status = ?");
            params.add(status);
        }
        if (start != null) {
            sql.append(" AND assign_time >= ?");
            params.add(Timestamp.valueOf(start));
        }
        if (end != null) {
            sql.append(" AND assign_time <= ?");
            params.add(Timestamp.valueOf(end));
        }

        return jdbcTemplate.query(sql.toString(), params.toArray(), new BeanPropertyRowMapper<>(DispatchTask.class));
    }

}

