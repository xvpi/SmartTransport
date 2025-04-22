package com.xvpi.smarttransportbackend.service;

import com.xvpi.smarttransportbackend.entity.AICommand;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AICommandService {
    List<AICommand> getUnprocessedCommands();
    void dispatchUnprocessedCommands();
    void generateAICommands(String currentTimeStr);
    Map<String, Integer> getCommandStatistics(LocalDateTime time);
    Optional<AICommand> getCommandById(Long commandId);
}
