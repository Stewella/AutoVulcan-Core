package com.example.analyzer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallGraphNode {
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("label")
    private String label;
    
    @JsonProperty("type")
    private String type; // "entry", "intermediate", "vulnerable"
}
