package com.example.analyzer.service;

import com.example.analyzer.client.FastApiClient;
import com.example.analyzer.model.AnalysisRequest;
import com.example.analyzer.model.AnalysisResult;
import com.example.analyzer.model.Dependency;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class AnalysisService {
    
    private static final Logger logger = LoggerFactory.getLogger(AnalysisService.class);
    
    @Value("${analyzer.repository.base:/app/repository}")
    private String repositoryBase;
    
    @Autowired
    private DownloadUtil downloadUtil;
    
    @Autowired
    private PomGenerator pomGenerator;
    
    @Autowired
    private PomDetector pomDetector;
    
    @Autowired
    private MavenExecutor mavenExecutor;
    
    @Autowired
    private SootUpEngine sootUpEngine;
    
    @Autowired
    private FastApiClient fastApiClient;
    
    private static final ObjectMapper objectMapper = new ObjectMapper()
        .enable(SerializationFeature.INDENT_OUTPUT);
    
    /**
     * Save analysis result to JSON file for inspection
     * Ensures results directory is created and saves the analysis output
     */
    private void saveResultToJson(String projectId, AnalysisResult result) {
        try {
            Path resultsDir = Paths.get(repositoryBase, projectId, "results");
            // Create results directory with parent directories if needed
            Files.createDirectories(resultsDir);
            logger.info("Created results directory: {}", resultsDir.toAbsolutePath());
            
            Path jsonFile = resultsDir.resolve("analysis-result.json");
            
            // Convert result to JSON string with proper formatting
            String jsonString = objectMapper.writeValueAsString(result);
            
            // Write JSON to file
            Files.write(jsonFile, jsonString.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            
            // Verify file was created
            if (Files.exists(jsonFile)) {
                long fileSize = Files.size(jsonFile);
                logger.info("✅ Analysis result saved successfully!");
                logger.info("   File: {}", jsonFile.toAbsolutePath());
                logger.info("   Size: {} bytes", fileSize);
            } else {
                logger.error("Failed to create JSON file: {}", jsonFile.toAbsolutePath());
            }
            
        } catch (Exception e) {
            logger.error("Failed to save analysis result to JSON", e);
            logger.error("Exception details: {}", e.getMessage());
        }
    }
    
    /**
     * Auto-generate projectId if not provided by user
     */
    private String generateProjectId(String repositoryUrl) {
        try {
            // Extract repository name from URL
            String repoName = repositoryUrl
                .substring(repositoryUrl.lastIndexOf("/") + 1)
                .replace(".zip", "")
                .replace("-", "_")
                .toLowerCase();
            
            // Combine with timestamp for uniqueness
            return repoName + "_" + System.currentTimeMillis();
        } catch (Exception e) {
            logger.warn("Could not generate projectId from URL, using UUID");
            return UUID.randomUUID().toString();
        }
    }
    
    @Async
    public void handleAnalysisAsync(AnalysisRequest request) {
        try {
            // Auto-generate projectId if not provided
            String projectId = request.getProjectId();
            if (projectId == null || projectId.trim().isEmpty()) {
                projectId = generateProjectId(request.getSourceZipUrl());
                request.setProjectId(projectId);
                logger.info("Auto-generated projectId: {}", projectId);
            }
            
            logger.info("Starting analysis for project: {}", projectId);
            
            Path repoDir = Paths.get(repositoryBase, projectId);
            Path srcDir = repoDir.resolve("src");
            Path buildDir = repoDir.resolve("build/classes");
            Path resultsDir = repoDir.resolve("results");
            
            // Pre-create results directory
            try {
                Files.createDirectories(resultsDir);
                logger.info("Pre-created results directory: {}", resultsDir.toAbsolutePath());
            } catch (Exception e) {
                logger.warn("Could not pre-create results directory", e);
            }
            
            // Step 1: Download and extract source ZIP
            logger.info("Downloading source from: {}", request.getSourceZipUrl());
            downloadUtil.downloadAndExtract(request.getSourceZipUrl(), srcDir);
            logger.info("✓ Source downloaded and extracted");
            
            // Step 2: Detect dependencies from downloaded pom.xml files
            List<Dependency> detectedDeps = pomDetector.detectDependencies(srcDir);
            logger.info("Detected {} dependencies from source pom.xml files", detectedDeps.size());
            
            // Merge with user-provided dependencies (user deps take precedence)
            List<Dependency> finalDeps = request.getDependencies();
            if (finalDeps == null || finalDeps.isEmpty()) {
                finalDeps = detectedDeps;
                logger.info("Using {} detected dependencies", detectedDeps.size());
            } else {
                logger.info("Using {} user-provided dependencies (ignoring {} detected)", 
                    finalDeps.size(), detectedDeps.size());
            }
            
            // Step 3: Generate pom.xml with merged dependencies
            logger.info("Generating pom.xml with {} dependencies", finalDeps.size());
            pomGenerator.generatePom(repoDir, finalDeps);
            logger.info("✓ POM generated");
            
            // Step 4: Compile with Maven
            logger.info("Compiling project with Maven...");
            mavenExecutor.compile(repoDir, buildDir);
            logger.info("✓ Compilation complete");
            
            // Step 5: Run SootUp analysis
            logger.info("Running SootUp static analysis...");
            AnalysisResult result = sootUpEngine.analyze(projectId, buildDir, finalDeps);
            logger.info("✓ SootUp analysis complete");
            
            // Validate result
            if (result == null) {
                logger.error("SootUp analysis returned null result!");
                result = new AnalysisResult();
                result.setProjectId(projectId);
            }
            
            // Step 6: Save result to JSON file
            logger.info("Saving analysis result to JSON file...");
            saveResultToJson(projectId, result);
            
            // Step 7: Send result back to FastAPI
            logger.info("Sending analysis result back to FastAPI backend...");
            fastApiClient.sendResultAsync(projectId, result);
            
            logger.info("🎉 Analysis completed successfully for project: {}", projectId);
            
        } catch (Exception e) {
            logger.error("❌ Analysis failed for project: {}", request.getProjectId(), e);
            logger.error("Exception type: {}", e.getClass().getName());
            logger.error("Exception message: {}", e.getMessage());
            // Optionally send error notification to FastAPI
            fastApiClient.sendErrorNotification(request.getProjectId(), e.getMessage());
        }
    }
}
