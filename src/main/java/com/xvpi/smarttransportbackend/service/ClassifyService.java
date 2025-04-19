package com.xvpi.smarttransportbackend.service;

import java.util.List;
import java.util.Map;

public interface ClassifyService {
    List<List<Integer>> getAllClassification();
    List<Integer> getClassificationByRoute(String oName, String dName);
    Map<String, Object> getClassificationByTimeIndex(String timeStr);
}
