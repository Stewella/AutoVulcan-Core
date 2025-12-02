package com.example.analyzer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CallGraphNode {
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("label")
    private String label;
    
    @JsonProperty("type")
    private String type; // "entry", "intermediate", "vulnerable"

    public CallGraphNode() {}

    public CallGraphNode(String id, String label, String type) {
        this.id = id;
        this.label = label;
        this.type = type;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
