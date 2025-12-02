package com.example.analyzer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class CFGMethod {
    @JsonProperty("class")
    private String className;
    
    @JsonProperty("method")
    private String method;
    
    @JsonProperty("nodes")
    private List<CFGNode> nodes;
    
    @JsonProperty("edges")
    private List<CFGEdge> edges;

    public CFGMethod() {}
    public CFGMethod(String className, String method, List<CFGNode> nodes, List<CFGEdge> edges) {
        this.className = className;
        this.method = method;
        this.nodes = nodes;
        this.edges = edges;
    }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public List<CFGNode> getNodes() { return nodes; }
    public void setNodes(List<CFGNode> nodes) { this.nodes = nodes; }
    public List<CFGEdge> getEdges() { return edges; }
    public void setEdges(List<CFGEdge> edges) { this.edges = edges; }
}
