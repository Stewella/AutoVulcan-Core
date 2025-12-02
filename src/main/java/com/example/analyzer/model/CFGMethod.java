package com.example.analyzer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CFGMethod {
    @JsonProperty("class")
    private String className;
    
    @JsonProperty("method")
    private String method;
    
    @JsonProperty("nodes")
    private List<CFGNode> nodes;
    
    @JsonProperty("edges")
    private List<CFGEdge> edges;
}
