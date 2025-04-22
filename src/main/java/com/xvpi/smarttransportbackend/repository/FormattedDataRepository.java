package com.xvpi.smarttransportbackend.repository;
        import com.xvpi.smarttransportbackend.entity.FormattedData;
        import org.apache.ibatis.annotations.Param;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.data.jpa.repository.Query;
        import org.springframework.stereotype.Repository;

@Repository
public interface FormattedDataRepository extends JpaRepository<FormattedData, Integer> {
        @Query("SELECT COUNT(DISTINCT t.plateNo) FROM FormattedData t WHERE t.formattedTime LIKE CONCAT(:date, '%')")
        int countDistinctPlateNoByDate(@Param("date") String date);

}

