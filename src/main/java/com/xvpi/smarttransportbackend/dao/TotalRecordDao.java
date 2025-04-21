package com.xvpi.smarttransportbackend.dao;

import com.xvpi.smarttransportbackend.entity.TotalRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class TotalRecordDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public TotalRecord findPreviousSegment(LocalDateTime time) {
        // 数据起始时间
        LocalDateTime baseTime = LocalDateTime.of(2024, 12, 29, 0, 0);
        Duration duration = Duration.between(baseTime, time);
        long minutes = duration.toMinutes();

        // 索引计算（每10分钟一条）
        int index = (int) (minutes / 10);

        // 取前一条记录
        if (index <= 0) throw new RuntimeException("该时间点无前一条记录");
        int targetId = index; // 注意：数据库记录从ID=1开始，所以我们取 index 对应的ID（因为 index-1 是我们想要的时间段）

        // 查询数据库
        String sql = "SELECT * FROM total_record WHERE id = ?";
        List<TotalRecord> result = jdbcTemplate.query(sql, new Object[]{targetId}, new BeanPropertyRowMapper<>(TotalRecord.class));
        return result.isEmpty() ? null : result.get(0);
    }
}