package com.example.analyzer.service;

import com.example.analyzer.client.FastApiClient;
import com.example.analyzer.model.AnalysisRequest;
import com.example.analyzer.model.AnalysisResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
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
    private MavenExecutor mavenExecutor;
    
    @Autowired
    private SootUpEngine sootUpEngine;
    
    @Autowired
    private FastApiClient fastApiClient;
    
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
            
            // Step 1: Download and extract source ZIP
            logger.info("Downloading source from: {}", request.getSourceZipUrl());
            downloadUtil.downloadAndExtract(request.getSourceZipUrl(), srcDir);
            
            // Step 2: Generate pom.xml
            logger.info("Generating pom.xml with {} dependencies", request.getDependencies().size());
            pomGenerator.generatePom(repoDir, request.getDependencies());
            
            // Step 3: Compile with Maven
            logger.info("Compiling project with Maven...");
            mavenExecutor.compile(repoDir, buildDir);
            
            // Step 4: Run SootUp analysis
            logger.info("Running SootUp static analysis...");
            AnalysisResult result = sootUpEngine.analyze(projectId, buildDir, request.getDependencies());
            
            // Step 5: Send result back to FastAPI
            logger.info("Sending analysis result back to FastAPI backend...");
            fastApiClient.sendResultAsync(projectId, result);
            
            logger.info("Analysis completed successfully for project: {}", projectId);
            
        } catch (Exception e) {
            logger.error("Analysis failed for project: {}", request.getProjectId(), e);
            // Optionally send error notification to FastAPI
            fastApiClient.sendErrorNotification(request.getProjectId(), e.getMessage());
        }
    }
}
