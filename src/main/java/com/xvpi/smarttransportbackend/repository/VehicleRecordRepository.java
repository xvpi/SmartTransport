package com.xvpi.smarttransportbackend.repository;
        import com.xvpi.smarttransportbackend.entity.VehicleRecord;
        import org.apache.ibatis.annotations.Param;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.data.jpa.repository.Query;
        import org.springframework.stereotype.Repository;
        import java.time.LocalDateTime;
        import java.util.List;

@Repository
public interface VehicleRecordRepository extends JpaRepository<VehicleRecord, Integer> {
        List<VehicleRecord> findByPassTimeAfter(LocalDateTime time);
        @Query("SELECT v.vehicleType, COUNT(v) " +
                "FROM VehicleRecord v " +
                "WHERE v.passTime BETWEEN :startTime AND :endTime " +
                "GROUP BY v.vehicleType")
        List<Object[]> countVehicleTypesBetween(
                @Param("startTime") LocalDateTime startTime,
                @Param("endTime") LocalDateTime endTime
        );

}

