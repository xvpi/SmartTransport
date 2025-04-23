package com.xvpi.smarttransportbackend.repository;

import com.xvpi.smarttransportbackend.entity.DispatchTask;
import com.xvpi.smarttransportbackend.entity.TotalRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DispatchTaskRepository extends JpaRepository<DispatchTask, Long> {
    List<DispatchTask> findByCommandId(Long commandId);
    List<DispatchTask> findByOfficerId(Long officerId);
    @Query("SELECT t FROM DispatchTask t WHERE " +
            "(:officerId IS NULL OR t.officerId = :officerId) AND " +
            "(:status IS NULL OR t.status = :status) AND " +
            "(:start IS NULL OR t.assignTime >= :start) AND " +
            "(:end IS NULL OR t.assignTime <= :end)")
    List<DispatchTask> findByCondition(@Param("officerId") Long officerId,
                                       @Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end,
                                       @Param("status") Integer status);

}
