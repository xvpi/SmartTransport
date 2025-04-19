package com.xvpi.smarttransportbackend.service;

import java.util.List;

public interface PredictionService {
    List<List<Integer>> getAllPredictions();
    List<Integer> getPredictionByRoute(String oName, String dName);
}

