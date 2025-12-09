package com.example.analyzer.controller;

import com.example.analyzer.model.AnalysisRequest;
import com.example.analyzer.service.AnalysisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class AnalysisController {
    
    private static final Logger logger = LoggerFactory.getLogger(AnalysisController.class);
    
    @Autowired
    private AnalysisService analysisService;
    
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
    
    @PostMapping("/analyze")
    public ResponseEntity<Map<String, Object>> analyze(@RequestBody AnalysisRequest request) {
        // Auto-generate projectId if not provided
        String projectId = request.getProjectId();
        if (projectId == null || projectId.trim().isEmpty()) {
            projectId = generateProjectId(request.getSourceZipUrl());
            request.setProjectId(projectId);
            logger.info("Auto-generated projectId: {}", projectId);
        }
        
        logger.info("Received analysis request for project: {}", projectId);
        
        try {
            // Trigger async analysis
            analysisService.handleAnalysisAsync(request);
            
            // Return immediate response with projectId (auto-generated or user-provided)
            Map<String, Object> response = new HashMap<>();
            response.put("status", "received");
            response.put("projectId", projectId);
            response.put("message", "Analysis started in background");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error processing analysis request for project: {}", projectId, e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("projectId", projectId);
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "healthy");
        response.put("service", "analyzer-service");
        return ResponseEntity.ok(response);
    }
}
