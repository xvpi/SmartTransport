package com.xvpi.smarttransportbackend.service;

import java.util.List;

public interface PredictionService {
    List<List<Integer>> getAllPredictions(String predictTime, String Param);

    List<Integer> getPredictionByRoute(String oName, String dName, String predictTime);
}

