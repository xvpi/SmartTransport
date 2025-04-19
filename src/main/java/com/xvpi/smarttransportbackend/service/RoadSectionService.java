
package com.xvpi.smarttransportbackend.service;

import com.xvpi.smarttransportbackend.entity.RoadSection;

import java.util.List;

public interface RoadSectionService {
    List<RoadSection> getAll();
    RoadSection getByOAndDName(String oName, String dName);
}
