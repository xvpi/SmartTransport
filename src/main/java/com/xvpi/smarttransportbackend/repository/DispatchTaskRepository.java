package com.xvpi.smarttransportbackend.repository;

import com.xvpi.smarttransportbackend.entity.DispatchTask;
import com.xvpi.smarttransportbackend.entity.TotalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DispatchTaskRepository extends JpaRepository<DispatchTask, Long> {
    List<DispatchTask> findByCommandId(Long commandId);
    List<DispatchTask> findByOfficerId(Long officerId);
}
