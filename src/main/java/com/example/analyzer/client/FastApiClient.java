package com.example.analyzer.client;

import com.example.analyzer.model.AnalysisResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class FastApiClient {
    
    private static final Logger logger = LoggerFactory.getLogger(FastApiClient.class);
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    
    @Value("${fastapi.backend.url:http://backend-fastapi.com}")
    private String backendUrl;
    
    @Value("${fastapi.backend.result-endpoint:/api/analysis/result}")
    private String resultEndpoint;
    
    @Value("${fastapi.backend.error-endpoint:/api/analysis/error}")
    private String errorEndpoint;
    
    @Value("${fastapi.backend.retry-attempts:3}")
    private int retryAttempts;
    
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;
    
    public FastApiClient() {
        this.client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();
        this.objectMapper = new ObjectMapper();
    }
    
    public void sendResultAsync(String projectId, AnalysisResult result) {
        logger.info("Sending analysis result for project: {} to FastAPI", projectId);
        
        new Thread(() -> {
            for (int attempt = 1; attempt <= retryAttempts; attempt++) {
                try {
                    sendResult(projectId, result);
                    logger.info("Successfully sent result for project: {}", projectId);
                    return;
                } catch (Exception e) {
                    logger.error("Attempt {}/{} failed to send result for project: {}", 
                               attempt, retryAttempts, projectId, e);
                    
                    if (attempt < retryAttempts) {
                        try {
                            Thread.sleep(5000 * attempt); // Exponential backoff
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                }
            }
            
            logger.error("Failed to send result for project: {} after {} attempts", 
                       projectId, retryAttempts);
        }).start();
    }
    
    private void sendResult(String projectId, AnalysisResult result) throws IOException {
        String url = backendUrl + resultEndpoint;
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("projectId", projectId);
        payload.put("analysis", result);
        
        String json = objectMapper.writeValueAsString(payload);
        
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
            .url(url)
            .post(body)
            .addHeader("Content-Type", "application/json")
            .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response);
            }
            
            logger.info("FastAPI response: {}", response.body().string());
        }
    }
    
    public void sendErrorNotification(String projectId, String errorMessage) {
        logger.info("Sending error notification for project: {}", projectId);
        
        new Thread(() -> {
            try {
                String url = backendUrl + errorEndpoint;
                
                Map<String, String> payload = new HashMap<>();
                payload.put("projectId", projectId);
                payload.put("error", errorMessage);
                payload.put("status", "failed");
                
                String json = objectMapper.writeValueAsString(payload);
                
                RequestBody body = RequestBody.create(json, JSON);
                Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();
                
                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        logger.info("Error notification sent successfully");
                    } else {
                        logger.warn("Failed to send error notification: {}", response.code());
                    }
                }
            } catch (Exception e) {
                logger.error("Failed to send error notification", e);
            }
        }).start();
    }
}
