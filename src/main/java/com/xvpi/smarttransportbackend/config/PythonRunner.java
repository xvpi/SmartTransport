package com.xvpi.smarttransportbackend.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PythonRunner {

    public static List<String> runPythonScript(String scriptPath) throws Exception {
        List<String> output = new ArrayList<>();
        File scriptFile = new File(scriptPath);
        File scriptDir = scriptFile.getParentFile();  // 获取脚本所在目录

        ProcessBuilder pb = new ProcessBuilder("python", scriptFile.getName());
        pb.directory(scriptDir); // 设置工作目录为脚本所在目录
        pb.redirectErrorStream(true);

        Process process = pb.start();

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
