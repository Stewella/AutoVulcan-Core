package com.example.analyzer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class AnalysisResult {
    @JsonProperty("projectId")
    private String projectId;
    
    @JsonProperty("callGraph")
    private CallGraph callGraph;
    
    @JsonProperty("cfg")
    private List<CFGMethod> cfg;

    public AnalysisResult() {}
    public AnalysisResult(String projectId, CallGraph callGraph, List<CFGMethod> cfg) {
        this.projectId = projectId;
        this.callGraph = callGraph;
        this.cfg = cfg;
    }

    public String getProjectId() { return projectId; }
    public void setProjectId(String projectId) { this.projectId = projectId; }
    public CallGraph getCallGraph() { return callGraph; }
    public void setCallGraph(CallGraph callGraph) { this.callGraph = callGraph; }
    public List<CFGMethod> getCfg() { return cfg; }
    public void setCfg(List<CFGMethod> cfg) { this.cfg = cfg; }
}
