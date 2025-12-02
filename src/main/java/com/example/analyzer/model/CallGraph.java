package com.example.analyzer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class CallGraph {
    @JsonProperty("nodes")
    private List<CallGraphNode> nodes;
    
    @JsonProperty("edges")
    private List<CallGraphEdge> edges;

    public CallGraph() {}

    public CallGraph(List<CallGraphNode> nodes, List<CallGraphEdge> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    public List<CallGraphNode> getNodes() { return nodes; }
    public void setNodes(List<CallGraphNode> nodes) { this.nodes = nodes; }

    public List<CallGraphEdge> getEdges() { return edges; }
    public void setEdges(List<CallGraphEdge> edges) { this.edges = edges; }
}
