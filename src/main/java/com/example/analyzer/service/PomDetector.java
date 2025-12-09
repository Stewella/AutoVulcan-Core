package com.example.analyzer.service;

import com.example.analyzer.model.Dependency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Detects and extracts dependencies from existing pom.xml files in downloaded projects
 * This helps compile multi-module projects like Petshop that have their own dependency declarations
 */
@Component
public class PomDetector {
    
    private static final Logger logger = LoggerFactory.getLogger(PomDetector.class);
    
    /**
     * Scan source directory recursively for pom.xml files and extract their dependencies
     * Merges all dependencies from all pom.xml files found
     */
    public List<Dependency> detectDependencies(Path srcDir) {
        Set<String> uniqueDeps = new HashSet<>();
        List<Dependency> allDependencies = new ArrayList<>();
        
        try {
            Files.walk(srcDir)
                .filter(path -> path.getFileName().toString().equals("pom.xml"))
                .forEach(pomPath -> {
                    try {
                        logger.info("Found pom.xml at: {}", pomPath);
                        List<Dependency> deps = extractDependenciesFromPom(pomPath.toFile());
                        
                        for (Dependency dep : deps) {
                            String key = dep.getGroupId() + ":" + dep.getArtifactId();
                            if (!uniqueDeps.contains(key)) {
                                uniqueDeps.add(key);
                                allDependencies.add(dep);
                                logger.info("  Added dependency: {}:{} v{}", 
                                    dep.getGroupId(), dep.getArtifactId(), dep.getVersion());
                            }
                        }
                    } catch (Exception e) {
                        logger.warn("Failed to parse pom.xml at {}: {}", pomPath, e.getMessage());
                    }
                });
            
            logger.info("Detected {} unique dependencies from {} pom.xml file(s)", 
                allDependencies.size(), uniqueDeps.size());
            
        } catch (Exception e) {
            logger.warn("Error scanning for pom.xml files: {}", e.getMessage());
        }
        
        return allDependencies;
    }
    
    /**
     * Parse a single pom.xml file and extract dependencies
     * Handles <dependency> elements with groupId, artifactId, and version
     */
    private List<Dependency> extractDependenciesFromPom(File pomFile) throws Exception {
        List<Dependency> dependencies = new ArrayList<>();
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        // Disable DTD processing for security
        dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
        dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document doc = builder.parse(pomFile);
        
        // Get all dependency elements
        NodeList depNodes = doc.getElementsByTagName("dependency");
        
        for (int i = 0; i < depNodes.getLength(); i++) {
            Element depElement = (Element) depNodes.item(i);
            
            // Skip dependencies with scope != "compile" or "runtime" or empty/omitted scope
            String scope = getChildElementText(depElement, "scope");
            if (scope != null && 
                !scope.isEmpty() && 
                !scope.equals("compile") && 
                !scope.equals("runtime") &&
                !scope.equals("provided")) {
                logger.debug("  Skipping dependency with scope: {}", scope);
                continue;
            }
            
            String groupId = getChildElementText(depElement, "groupId");
            String artifactId = getChildElementText(depElement, "artifactId");
            String version = getChildElementText(depElement, "version");
            
            if (groupId != null && artifactId != null) {
                Dependency dep = new Dependency();
                dep.setGroupId(groupId);
                dep.setArtifactId(artifactId);
                dep.setVersion(version != null ? version : "LATEST");
                dependencies.add(dep);
            }
        }
        
        return dependencies;
    }
    
    /**
     * Helper method to extract text content from a child element
     */
    private String getChildElementText(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() > 0) {
            String text = nodes.item(0).getTextContent().trim();
            return text.isEmpty() ? null : text;
        }
        return null;
    }
}
