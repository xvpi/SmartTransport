package com.xvpi.smarttransportbackend.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PythonRunner {

    public static List<String> runPythonScript(String scriptPath, String... args) throws Exception {
        List<String> command = new ArrayList<>();
        command.add("python");
        command.add(scriptPath);
        if (args != null) {
            Collections.addAll(command, args);
        }
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        List<String> output = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("PYTHON >>> " + line);
                output.add(line.trim());
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Python script exited with code " + exitCode);
        }

        return output;
    }

}
