package com.example.analyzer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallGraph {
    @JsonProperty("nodes")
    private List<CallGraphNode> nodes;
    
    @JsonProperty("edges")
    private List<CallGraphEdge> edges;
}
