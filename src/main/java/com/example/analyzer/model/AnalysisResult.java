package com.example.analyzer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisResult {
    @JsonProperty("projectId")
    private String projectId;
    
    @JsonProperty("callGraph")
    private CallGraph callGraph;
    
    @JsonProperty("cfg")
    private List<CFGMethod> cfg;
}
