package com.xvpi.smarttransportbackend.service.impl;

import com.xvpi.smarttransportbackend.dao.TotalRecordDao;
import com.xvpi.smarttransportbackend.entity.TotalRecord;
import com.xvpi.smarttransportbackend.repository.TotalRecordRepository;
import com.xvpi.smarttransportbackend.service.TotalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TotalRecordServiceImpl implements TotalRecordService {

    private final TotalRecordRepository totalRecordRepository;

    @Override
    // 获取所有记录
    public List<TotalRecord> getAllRecords() {
        return totalRecordRepository.findAll();
    }
    @Override
    // 根据时间范围获取记录
    public List<TotalRecord> getRecordsByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return totalRecordRepository.findByStartTimeBetween(startTime, endTime);
    }
    @Autowired
    private TotalRecordDao totalRecordDao;

    public TotalRecord getPreviousRecord(LocalDateTime time) {
        return totalRecordDao.findPreviousSegment(time);
    }

}
