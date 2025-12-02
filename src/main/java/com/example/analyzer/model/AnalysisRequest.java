package com.example.analyzer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class AnalysisRequest {
    @JsonProperty("projectId")
    private String projectId;
    
    @JsonProperty("sourceZipUrl")
    private String sourceZipUrl;
    
    @JsonProperty("dependencies")
    private List<Dependency> dependencies;

    public AnalysisRequest() {}

    public AnalysisRequest(String projectId, String sourceZipUrl, List<Dependency> dependencies) {
        this.projectId = projectId;
        this.sourceZipUrl = sourceZipUrl;
        this.dependencies = dependencies;
    }

    public String getProjectId() { return projectId; }
    public void setProjectId(String projectId) { this.projectId = projectId; }

    public String getSourceZipUrl() { return sourceZipUrl; }
    public void setSourceZipUrl(String sourceZipUrl) { this.sourceZipUrl = sourceZipUrl; }

    public List<Dependency> getDependencies() { return dependencies; }
    public void setDependencies(List<Dependency> dependencies) { this.dependencies = dependencies; }
}
