package com.xvpi.smarttransportbackend.repository;
        import com.xvpi.smarttransportbackend.entity.VehicleRecord;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.stereotype.Repository;
        import java.time.LocalDateTime;
        import java.util.List;

@Repository
public interface VehicleRecordRepository extends JpaRepository<VehicleRecord, Integer> {
        List<VehicleRecord> findByPassTimeAfter(LocalDateTime time);
}

