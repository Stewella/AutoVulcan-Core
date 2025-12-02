package com.example.analyzer.service;

import com.example.analyzer.model.Dependency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Component
public class PomGenerator {
    
    private static final Logger logger = LoggerFactory.getLogger(PomGenerator.class);
    
    public void generatePom(Path repoDir, List<Dependency> dependencies) throws IOException {
        logger.info("Generating pom.xml with {} dependencies", dependencies.size());
        
        Path pomFile = repoDir.resolve("pom.xml");
        
        StringBuilder pom = new StringBuilder();
        pom.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        pom.append("<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n");
        pom.append("         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
        pom.append("         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0\n");
        pom.append("         http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n");
        pom.append("  <modelVersion>4.0.0</modelVersion>\n\n");
        
        pom.append("  <groupId>com.analyzed</groupId>\n");
        pom.append("  <artifactId>analyzed-project</artifactId>\n");
        pom.append("  <version>1.0.0</version>\n\n");
        
        pom.append("  <properties>\n");
        pom.append("    <maven.compiler.source>17</maven.compiler.source>\n");
        pom.append("    <maven.compiler.target>17</maven.compiler.target>\n");
        pom.append("    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\n");
        pom.append("  </properties>\n\n");
        
        pom.append("  <dependencies>\n");
        for (Dependency dep : dependencies) {
            pom.append("    <dependency>\n");
            pom.append("      <groupId>").append(dep.getGroupId()).append("</groupId>\n");
            pom.append("      <artifactId>").append(dep.getArtifactId()).append("</artifactId>\n");
            if (dep.getVersion() != null && !dep.getVersion().isEmpty()) {
                pom.append("      <version>").append(dep.getVersion()).append("</version>\n");
            }
            pom.append("    </dependency>\n");
        }
        pom.append("  </dependencies>\n\n");
        
        pom.append("  <build>\n");
        pom.append("    <sourceDirectory>src</sourceDirectory>\n");
        pom.append("    <outputDirectory>build/classes</outputDirectory>\n");
        pom.append("    <plugins>\n");
        pom.append("      <plugin>\n");
        pom.append("        <groupId>org.apache.maven.plugins</groupId>\n");
        pom.append("        <artifactId>maven-compiler-plugin</artifactId>\n");
        pom.append("        <version>3.11.0</version>\n");
        pom.append("        <configuration>\n");
        pom.append("          <source>17</source>\n");
        pom.append("          <target>17</target>\n");
        pom.append("        </configuration>\n");
        pom.append("      </plugin>\n");
        pom.append("    </plugins>\n");
        pom.append("  </build>\n");
        pom.append("</project>\n");
        
        try (FileWriter writer = new FileWriter(pomFile.toFile())) {
            writer.write(pom.toString());
        }
        
        logger.info("Generated pom.xml at: {}", pomFile);
    }
}
