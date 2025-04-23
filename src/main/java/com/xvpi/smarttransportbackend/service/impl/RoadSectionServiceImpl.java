// RoadSectionServiceImpl.java
package com.xvpi.smarttransportbackend.service.impl;

import com.xvpi.smarttransportbackend.entity.CameraDevice;
import com.xvpi.smarttransportbackend.entity.RoadSection;
import com.xvpi.smarttransportbackend.repository.RoadSectionDao;
import com.xvpi.smarttransportbackend.repository.RoadSectionRepository;
import com.xvpi.smarttransportbackend.service.RoadSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoadSectionServiceImpl implements RoadSectionService {

    @Autowired
    private RoadSectionDao roadSectionDao;
    @Autowired
    private RoadSectionRepository roadSectionRepository;
    @Override
    public List<RoadSection> getAll() {
        return roadSectionDao.getAll();
    }

    @Override
    public RoadSection getByOAndDName(String oName, String dName) {
        return roadSectionDao.getByOAndDName(oName, dName);
    }
    @Override
    public RoadSection findByRoute(String route){
        if (route != null && route.contains("-")) {
            String[] parts = route.split("-");
            if (parts.length == 2) {
                return roadSectionDao.getByOAndDName(parts[0], parts[1]);
            }
        }
        return null;
    }
    @Override
    public RoadSection addRoad(RoadSection roadSection){
        return roadSectionRepository.save(roadSection);
    }
    @Override
    public void deleteRoad(Integer id){
        roadSectionRepository.deleteById(id);
    }
    @Override
    public RoadSection updateRoad(RoadSection roadSection) {
        RoadSection existing = roadSectionRepository.findById(roadSection.getId())
                .orElseThrow(() -> new RuntimeException("Road not found"));
        existing.setOName(roadSection.getOName());
        existing.setDName(roadSection.getDName());
        existing.setOGis(roadSection.getOGis());
        existing.setDGis(roadSection.getDGis());
        existing.setDistance(roadSection.getDistance());
        existing.setPathGis(roadSection.getPathGis());
        existing.setRoadCapacity(roadSection.getRoadCapacity());
        return roadSectionRepository.save(existing);
    }

}
