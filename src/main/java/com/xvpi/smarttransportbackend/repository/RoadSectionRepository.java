package com.xvpi.smarttransportbackend.repository;

import com.xvpi.smarttransportbackend.entity.RoadSection;
import com.xvpi.smarttransportbackend.entity.TotalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoadSectionRepository extends JpaRepository<RoadSection, Integer> {

}
