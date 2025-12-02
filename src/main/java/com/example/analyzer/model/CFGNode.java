package com.example.analyzer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CFGNode {
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("stmt")
    private String stmt;
}
