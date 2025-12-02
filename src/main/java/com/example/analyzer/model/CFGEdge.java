package com.example.analyzer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CFGEdge {
    @JsonProperty("source")
    private String source;
    
    @JsonProperty("target")
    private String target;

    public CFGEdge() {}
    public CFGEdge(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }
}
