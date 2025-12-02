package com.example.analyzer.service;

import com.example.analyzer.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import sootup.callgraph.CallGraph;
import sootup.callgraph.CallGraphAlgorithm;
import sootup.callgraph.ClassHierarchyAnalysisAlgorithm;
import sootup.core.inputlocation.AnalysisInputLocation;
import sootup.core.model.SootMethod;
import sootup.core.signatures.MethodSignature;
import sootup.core.types.ClassType;
import sootup.java.bytecode.frontend.inputlocation.JavaClassPathAnalysisInputLocation;
import sootup.java.core.JavaIdentifierFactory;
import sootup.java.core.JavaSootClass;
import sootup.java.core.views.JavaView;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class SootUpEngine {
    
    private static final Logger logger = LoggerFactory.getLogger(SootUpEngine.class);
    
    public AnalysisResult analyze(String projectId, Path classesDir, List<Dependency> dependencies) {
        logger.info("Starting SootUp analysis for project: {}", projectId);
        
        try {
            // Step 1: Load bytecode into SootUp
            AnalysisInputLocation inputLocation = new JavaClassPathAnalysisInputLocation(classesDir.toString());
            JavaView view = new JavaView(Collections.singletonList(inputLocation));
            
            // Step 2: Find entry points (main methods or @RestController/@RequestMapping methods)
            List<MethodSignature> entryPoints = findEntryPoints(view);
            logger.info("Found {} entry points", entryPoints.size());
            
            if (entryPoints.isEmpty()) {
                logger.warn("No entry points found, using all methods");
                // Fallback: use first method found
                entryPoints = findAllMethods(view);
            }
            
            // Step 3: Build call graph using CHA
            CallGraphAlgorithm cha = new ClassHierarchyAnalysisAlgorithm(view);
            CallGraph cg = cha.initialize(entryPoints);
            
            logger.info("Call graph built with {} methods", cg.callCount());
            
            // Step 4: Extract call graph nodes and edges
            CallGraph callGraph = extractCallGraph(cg, view, dependencies);
            
            // Step 5: Extract CFG for each method
            List<CFGMethod> cfgMethods = extractCFG(view);
            
            // Step 6: Build result
            AnalysisResult result = new AnalysisResult();
            result.setProjectId(projectId);
            result.setCallGraph(callGraph);
            result.setCfg(cfgMethods);
            
            logger.info("Analysis complete for project: {}", projectId);
            return result;
            
        } catch (Exception e) {
            logger.error("SootUp analysis failed", e);
            throw new RuntimeException("Analysis failed: " + e.getMessage(), e);
        }
    }
    
    private List<MethodSignature> findEntryPoints(JavaView view) {
        List<MethodSignature> entryPoints = new ArrayList<>();
        JavaIdentifierFactory idf = JavaIdentifierFactory.getInstance();
        
        for (JavaSootClass cls : view.getClasses()) {
            for (SootMethod method : cls.getMethods()) {
                // Check for main method
                if (method.getName().equals("main") && method.isStatic() && method.isPublic()) {
                    entryPoints.add(method.getSignature());
                }
                
                // Check for Spring annotations (simplified - would need annotation analysis)
                String methodName = method.getName();
                if (methodName.contains("Controller") || methodName.contains("Mapping") || 
                    methodName.contains("endpoint") || methodName.contains("handler")) {
                    entryPoints.add(method.getSignature());
                }
            }
        }
        
        return entryPoints;
    }
    
    private List<MethodSignature> findAllMethods(JavaView view) {
        List<MethodSignature> methods = new ArrayList<>();
        for (JavaSootClass cls : view.getClasses()) {
            for (SootMethod method : cls.getMethods()) {
                methods.add(method.getSignature());
            }
        }
        return methods.stream().limit(1).collect(Collectors.toList());
    }
    
    private CallGraph extractCallGraph(CallGraph cg, JavaView view, List<Dependency> dependencies) {
        Set<String> dependencyPackages = dependencies.stream()
            .map(d -> d.getGroupId())
            .collect(Collectors.toSet());
        
        Map<String, CallGraphNode> nodeMap = new HashMap<>();
        List<CallGraphEdge> edges = new ArrayList<>();
        AtomicInteger nodeCounter = new AtomicInteger(1);
        
        // Extract all calls
        for (MethodSignature source : cg.getMethodSignatures()) {
            Collection<sootup.callgraph.CallGraph.Call> calls = cg.callsFrom(source);
            
            for (sootup.callgraph.CallGraph.Call call : calls) {
                MethodSignature target = call.getTargetMethodSignature();
                
                // Create nodes
                String sourceId = getOrCreateNode(source, nodeMap, nodeCounter, dependencyPackages);
                String targetId = getOrCreateNode(target, nodeMap, nodeCounter, dependencyPackages);
                
                // Create edge
                edges.add(new CallGraphEdge(sourceId, targetId));
            }
        }
        
        CallGraph result = new CallGraph();
        result.setNodes(new ArrayList<>(nodeMap.values()));
        result.setEdges(edges);
        
        return result;
    }
    
    private String getOrCreateNode(MethodSignature sig, Map<String, CallGraphNode> nodeMap, 
                                   AtomicInteger counter, Set<String> dependencyPackages) {
        String sigStr = sig.toString();
        
        if (nodeMap.containsKey(sigStr)) {
            return nodeMap.get(sigStr).getId();
        }
        
        String nodeId = "n" + counter.getAndIncrement();
        String className = sig.getDeclClassType().getFullyQualifiedName();
        String methodName = sig.getName();
        String label = className + "." + methodName + "()";
        
        // Classify node type
        String type;
        if (methodName.equals("main") || methodName.contains("Controller") || 
            methodName.contains("endpoint")) {
            type = "entry";
        } else if (isVulnerableMethod(className, dependencyPackages)) {
            type = "vulnerable";
        } else {
            type = "intermediate";
        }
        
        CallGraphNode node = new CallGraphNode(nodeId, label, type);
        nodeMap.put(sigStr, node);
        
        return nodeId;
    }
    
    private boolean isVulnerableMethod(String className, Set<String> dependencyPackages) {
        for (String pkg : dependencyPackages) {
            if (className.startsWith(pkg.replace('-', '.'))) {
                return true;
            }
        }
        return false;
    }
    
    private List<CFGMethod> extractCFG(JavaView view) {
        List<CFGMethod> cfgMethods = new ArrayList<>();
        
        for (JavaSootClass cls : view.getClasses()) {
            for (SootMethod method : cls.getMethods()) {
                try {
                    CFGMethod cfgMethod = extractMethodCFG(cls, method);
                    cfgMethods.add(cfgMethod);
                } catch (Exception e) {
                    logger.debug("Could not extract CFG for method: {}", method.getSignature(), e);
                }
            }
        }
        
        return cfgMethods;
    }
    
    private CFGMethod extractMethodCFG(JavaSootClass cls, SootMethod method) {
        List<CFGNode> nodes = new ArrayList<>();
        List<CFGEdge> edges = new ArrayList<>();
        
        try {
            // Extract Jimple statements
            List<String> stmts = new ArrayList<>();
            method.getBody().getStmts().forEach(stmt -> stmts.add(stmt.toString()));
            
            // Build CFG nodes from statements
            for (int i = 0; i < stmts.size(); i++) {
                nodes.add(new CFGNode("c" + (i + 1), stmts.get(i)));
                
                // Simple sequential edges
                if (i < stmts.size() - 1) {
                    edges.add(new CFGEdge("c" + (i + 1), "c" + (i + 2)));
                }
            }
            
        } catch (Exception e) {
            // Method has no body
            nodes.add(new CFGNode("c1", "<<no body>>"));
        }
        
        CFGMethod cfgMethod = new CFGMethod();
        cfgMethod.setClassName(cls.getType().toString());
        cfgMethod.setMethod(method.getName() + "()");
        cfgMethod.setNodes(nodes);
        cfgMethod.setEdges(edges);
        
        return cfgMethod;
    }
}
