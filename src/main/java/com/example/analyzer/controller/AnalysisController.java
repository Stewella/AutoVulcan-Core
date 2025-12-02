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

@RestController
@RequestMapping("/api")
public class AnalysisController {
    
    private static final Logger logger = LoggerFactory.getLogger(AnalysisController.class);
    
    @Autowired
    private AnalysisService analysisService;
    
    @PostMapping("/analyze")
    public ResponseEntity<Map<String, Object>> analyze(@RequestBody AnalysisRequest request) {
        logger.info("Received analysis request for project: {}", request.getProjectId());
        
        try {
            // Trigger async analysis
            analysisService.handleAnalysisAsync(request);
            
            // Return immediate response
            Map<String, Object> response = new HashMap<>();
            response.put("status", "received");
            response.put("projectId", request.getProjectId());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error processing analysis request for project: {}", request.getProjectId(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("projectId", request.getProjectId());
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
