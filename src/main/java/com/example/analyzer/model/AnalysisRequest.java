package com.example.analyzer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class AnalysisRequest {
    @JsonProperty("projectId")
    private String projectId;
    
    @JsonProperty("sourceZipUrl")
    private String sourceZipUrl;
    
    @JsonProperty("dependencies")
    private List<Dependency> dependencies;
}
