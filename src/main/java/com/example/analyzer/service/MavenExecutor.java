package com.example.analyzer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
public class MavenExecutor {
    
    private static final Logger logger = LoggerFactory.getLogger(MavenExecutor.class);
    
    public void compile(Path repoDir, Path outputDir) throws IOException, InterruptedException {
        logger.info("Compiling project at: {}", repoDir);
        
        // Ensure output directory exists
        Files.createDirectories(outputDir);
        
        // Determine mvn command (mvn or mvnw)
        String mvnCommand = determineMavenCommand(repoDir);
        
        // Build command
        List<String> command = new ArrayList<>();
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            command.add("cmd.exe");
            command.add("/c");
        } else {
            command.add("/bin/sh");
            command.add("-c");
        }
        
        command.add(mvnCommand + " clean compile -DskipTests");
        
        // Execute Maven
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(repoDir.toFile());
        pb.redirectErrorStream(true);
        
        Process process = pb.start();
        
        // Capture output
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logger.debug("Maven: {}", line);
            }
        }
        
        int exitCode = process.waitFor();
        
        if (exitCode != 0) {
            throw new IOException("Maven compilation failed with exit code: " + exitCode);
        }
        
        logger.info("Compilation successful. Output at: {}", outputDir);
    }
    
    private String determineMavenCommand(Path repoDir) {
        // Check for Maven Wrapper
        Path mvnw = repoDir.resolve("mvnw");
        Path mvnwCmd = repoDir.resolve("mvnw.cmd");
        
        if (Files.exists(mvnw) || Files.exists(mvnwCmd)) {
            return System.getProperty("os.name").toLowerCase().contains("windows") ? "mvnw.cmd" : "./mvnw";
        }
        
        // Fall back to system Maven
        return "mvn";
    }
}
