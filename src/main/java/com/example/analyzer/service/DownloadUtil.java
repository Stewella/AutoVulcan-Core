package com.example.analyzer.service;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;

@Component
public class DownloadUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(DownloadUtil.class);
    
    public void downloadAndExtract(String url, Path destDir) throws IOException {
        logger.info("Downloading ZIP from: {}", url);
        
        // Create destination directory
        Files.createDirectories(destDir);
        
        // Download to temp file
        Path tempZip = Files.createTempFile("source-", ".zip");
        try {
            FileUtils.copyURLToFile(new URL(url), tempZip.toFile(), 30000, 60000);
            logger.info("Downloaded {} bytes", Files.size(tempZip));
            
            // Extract ZIP
            extractZip(tempZip, destDir);
            
            logger.info("Extracted source to: {}", destDir);
        } finally {
            Files.deleteIfExists(tempZip);
        }
    }
    
    private void extractZip(Path zipFile, Path destDir) throws IOException {
        try (ZipFile zip = new ZipFile(zipFile.toFile())) {
            Enumeration<ZipArchiveEntry> entries = zip.getEntries();
            
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                Path entryPath = destDir.resolve(entry.getName());
                
                if (entry.isDirectory()) {
                    Files.createDirectories(entryPath);
                } else {
                    Files.createDirectories(entryPath.getParent());
                    try (InputStream in = zip.getInputStream(entry);
                         OutputStream out = new FileOutputStream(entryPath.toFile())) {
                        IOUtils.copy(in, out);
                    }
                }
            }
        }
    }
}
