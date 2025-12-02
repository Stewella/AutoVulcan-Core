package com.example.analyzer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CFGNode {
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("stmt")
    private String stmt;

    public CFGNode() {}
    public CFGNode(String id, String stmt) {
        this.id = id;
        this.stmt = stmt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getStmt() { return stmt; }
    public void setStmt(String stmt) { this.stmt = stmt; }
}
