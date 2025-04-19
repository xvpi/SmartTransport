package com.xvpi.smarttransportbackend.repository;

import com.xvpi.smarttransportbackend.entity.RoadSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoadSectionDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<RoadSection> getAll() {
        String sql = "SELECT * FROM road_section";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(RoadSection.class));
    }

    public RoadSection getByOAndDName(String oName, String dName) {
        String sql = "SELECT * FROM road_section WHERE o_name = ? AND d_name = ?";
        List<RoadSection> list = jdbcTemplate.query(sql, new Object[]{oName, dName}, new BeanPropertyRowMapper<>(RoadSection.class));
        return list.isEmpty() ? null : list.get(0);
    }
}
