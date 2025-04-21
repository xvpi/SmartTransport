package com.xvpi.smarttransportbackend.service;

import com.xvpi.smarttransportbackend.entity.AICommand;

import java.util.List;

public interface AICommandService {
    List<AICommand> getUnprocessedCommands();
    void markAsProcessed(Long id);
    void generateAICommands(String currentTimeStr);
}
