package com.xvpi.smarttransportbackend.repository;
        import com.xvpi.smarttransportbackend.entity.TotalRecord;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.stereotype.Repository;

        import java.time.LocalDateTime;
        import java.util.List;

@Repository
public interface TotalRecordRepository extends JpaRepository<TotalRecord, Integer> {
        List<TotalRecord> findByStartTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}
