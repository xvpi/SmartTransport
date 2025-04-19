// RoadSectionServiceImpl.java
package com.xvpi.smarttransportbackend.service.impl;

import com.xvpi.smarttransportbackend.entity.RoadSection;
import com.xvpi.smarttransportbackend.repository.RoadSectionDao;
import com.xvpi.smarttransportbackend.service.RoadSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoadSectionServiceImpl implements RoadSectionService {

    @Autowired
    private RoadSectionDao roadSectionDao;

    @Override
    public List<RoadSection> getAll() {
        return roadSectionDao.getAll();
    }

    @Override
    public RoadSection getByOAndDName(String oName, String dName) {
        return roadSectionDao.getByOAndDName(oName, dName);
    }
}
