package com.xvpi.smarttransportbackend.repository;

import com.xvpi.smarttransportbackend.entity.AICommand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AICommandRepository extends JpaRepository<AICommand, Long> {
    List<AICommand> findByProcessed(Integer number);
    @Query("SELECT c FROM AICommand c WHERE c.processed < c.officerCount")
    List<AICommand> findByProcessedLessThanOfficerCount();

    List<AICommand> findByStatus(boolean b);
}

