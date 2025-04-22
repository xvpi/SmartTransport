package com.xvpi.smarttransportbackend.service;
import com.xvpi.smarttransportbackend.entity.TotalRecord;

import java.time.LocalDateTime;
import java.util.List;

public interface TotalRecordService {
    List<TotalRecord> getAllRecords();
    List<TotalRecord> getRecordsByTimeRange(LocalDateTime startTime, LocalDateTime endTime);
    TotalRecord getPreviousRecord(LocalDateTime time);
    int countDistinctPlateNoByDate(String date);
}
