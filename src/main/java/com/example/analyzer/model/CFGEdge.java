package com.example.analyzer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CFGEdge {
    @JsonProperty("source")
    private String source;
    
    @JsonProperty("target")
    private String target;
}
