package com.xvpi.smarttransportbackend.repository;
        import com.xvpi.smarttransportbackend.entity.FormattedData;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.stereotype.Repository;

@Repository
public interface FormattedDataRepository extends JpaRepository<FormattedData, Integer> {}

