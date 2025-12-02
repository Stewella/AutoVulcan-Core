package org.example.sootup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import spark.Spark;

import javax.servlet.MultipartConfigElement;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class AnalyzerServer {

    private static final int MAX_FILE_SIZE = 100 * 1024 * 1024; // 100MB
    private static final int MAX_REQUEST_SIZE = 150 * 1024 * 1024; // 150MB

    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 4567;
        Spark.port(port);

        // Serve static files (index.html)
        Spark.get("/", (req, res) -> getIndexHtml());

        // Health check endpoint
        Spark.get("/health", (req, res) -> {
            res.type("application/json");
            return new Gson().toJson(Map.of("status", "ok", "message", "Analyzer server is running"));
        });

        // Main analysis endpoint
        Spark.post("/analyze", (req, res) -> {
            res.type("application/json");
            try {
                Map<String, Object> result = handleAnalyzeRequest(req);
                return new GsonBuilder().setPrettyPrinting().create().toJson(result);
            } catch (Exception e) {
                Map<String, Object> error = new LinkedHashMap<>();
                error.put("error", e.getMessage());
                error.put("timestamp", System.currentTimeMillis());
                res.status(400);
                return new GsonBuilder().setPrettyPrinting().create().toJson(error);
            }
        });

        System.out.println("✓ Analyzer Server started on http://localhost:" + port);
        System.out.println("✓ Open browser and go to http://localhost:" + port);
    }

    private static Map<String, Object> handleAnalyzeRequest(spark.Request req) throws Exception {
        // Extract form fields
        String mainClass = req.queryParams("mainClass");
        String depPrefix = req.queryParams("depPrefix");

        if (mainClass == null || mainClass.trim().isEmpty()) {
            throw new IllegalArgumentException("Parameter 'mainClass' is required");
        }
        if (depPrefix == null || depPrefix.trim().isEmpty()) {
            throw new IllegalArgumentException("Parameter 'depPrefix' is required");
        }

        // Create temp directories
        Path tempDir = Files.createTempDirectory("analyzer_");
        Path sourceDir = tempDir.resolve("sources");
        Path libsDir = tempDir.resolve("libs");
        Path outputDir = tempDir.resolve("output");

        Files.createDirectories(sourceDir);
        Files.createDirectories(libsDir);
        Files.createDirectories(outputDir);

        try {
            // Extract uploaded sources (zip file)
            byte[] sourceZip = req.raw().getPart("sources").getInputStream().readAllBytes();
            extractZip(sourceZip, sourceDir);

            // Extract libs if provided
            try {
                byte[] libsZip = req.raw().getPart("libs").getInputStream().readAllBytes();
                extractZip(libsZip, libsDir);
            } catch (Exception e) {
                // libs is optional
            }

            // Run analyzer
            Map<String, Object> analysisResult = runAnalyzer(sourceDir, mainClass, depPrefix, libsDir);

            // Clean up
            deleteDirectory(tempDir.toFile());

            return analysisResult;

        } catch (Exception e) {
            deleteDirectory(tempDir.toFile());
            throw e;
        }
    }

    private static void extractZip(byte[] zipData, Path targetDir) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipData))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path entryPath = targetDir.resolve(entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectories(entryPath);
                } else {
                    Files.createDirectories(entryPath.getParent());
                    Files.copy(zis, entryPath);
                }
            }
        }
    }

    private static Map<String, Object> runAnalyzer(Path sourceDir, String mainClass, String depPrefix, Path libsDir) throws Exception {
        // Call AnalyzerWithFilter logic
        Process process = new ProcessBuilder(
                "java",
                "-cp", "target/SootUpAnalysis-1.0.0-jar-with-dependencies.jar",
                "org.example.sootup.AnalyzerWithFilter",
                sourceDir.toAbsolutePath().toString(),
                mainClass,
                depPrefix,
                libsDir.toAbsolutePath().toString()
        ).redirectErrorStream(true).start();

        int exitCode = process.waitFor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", exitCode == 0 ? "success" : "failed");
        result.put("exit_code", exitCode);
        result.put("console_output", output.toString());

        // Try to read generated JSON if analysis succeeded
        if (exitCode == 0) {
            Path jsonFile = Paths.get("output/analysis_filtered.json");
            if (Files.exists(jsonFile)) {
                String jsonContent = new String(Files.readAllBytes(jsonFile));
                result.put("analysis", new Gson().fromJson(jsonContent, Object.class));
            }
        }

        return result;
    }

    private static void deleteDirectory(File file) {
        if (!file.exists()) return;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) deleteDirectory(f);
            }
        }
        file.delete();
    }

    private static String getIndexHtml() {
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Code Analyzer</title>
                    <style>
                        * { margin: 0; padding: 0; box-sizing: border-box; }
                        body {
                            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
                            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                            min-height: 100vh;
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            padding: 20px;
                        }
                        .container {
                            background: white;
                            border-radius: 10px;
                            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
                            padding: 40px;
                            max-width: 600px;
                            width: 100%;
                        }
                        h1 {
                            color: #333;
                            margin-bottom: 10px;
                            font-size: 28px;
                        }
                        .subtitle {
                            color: #666;
                            margin-bottom: 30px;
                            font-size: 14px;
                        }
                        .form-group {
                            margin-bottom: 20px;
                        }
                        label {
                            display: block;
                            color: #333;
                            font-weight: 600;
                            margin-bottom: 8px;
                            font-size: 14px;
                        }
                        input[type="text"], input[type="file"] {
                            width: 100%;
                            padding: 12px;
                            border: 1px solid #ddd;
                            border-radius: 5px;
                            font-size: 14px;
                            font-family: inherit;
                            transition: border-color 0.3s;
                        }
                        input[type="text"]:focus, input[type="file"]:focus {
                            outline: none;
                            border-color: #667eea;
                            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
                        }
                        .info-text {
                            font-size: 12px;
                            color: #999;
                            margin-top: 5px;
                        }
                        button {
                            width: 100%;
                            padding: 12px;
                            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                            color: white;
                            border: none;
                            border-radius: 5px;
                            font-size: 16px;
                            font-weight: 600;
                            cursor: pointer;
                            transition: transform 0.2s, box-shadow 0.2s;
                        }
                        button:hover {
                            transform: translateY(-2px);
                            box-shadow: 0 5px 20px rgba(102, 126, 234, 0.4);
                        }
                        button:disabled {
                            background: #ccc;
                            cursor: not-allowed;
                            transform: none;
                        }
                        #result {
                            margin-top: 30px;
                            padding: 20px;
                            background: #f5f5f5;
                            border-radius: 5px;
                            display: none;
                            max-height: 600px;
                            overflow-y: auto;
                        }
                        #result.show {
                            display: block;
                        }
                        pre {
                            background: white;
                            padding: 15px;
                            border-radius: 5px;
                            overflow-x: auto;
                            font-size: 12px;
                            line-height: 1.4;
                        }
                        .error {
                            color: #d32f2f;
                            background: #ffebee;
                            padding: 12px;
                            border-radius: 5px;
                            margin-top: 10px;
                            border-left: 4px solid #d32f2f;
                        }
                        .loading {
                            display: none;
                            text-align: center;
                            margin-top: 20px;
                        }
                        .spinner {
                            border: 3px solid #f3f3f3;
                            border-top: 3px solid #667eea;
                            border-radius: 50%;
                            width: 40px;
                            height: 40px;
                            animation: spin 1s linear infinite;
                            margin: 0 auto;
                        }
                        @keyframes spin {
                            0% { transform: rotate(0deg); }
                            100% { transform: rotate(360deg); }
                        }
                        .loading.show { display: block; }
                        .tab-buttons {
                            display: flex;
                            gap: 10px;
                            margin-bottom: 15px;
                        }
                        .tab-btn {
                            flex: 1;
                            padding: 8px;
                            background: #e0e0e0;
                            border: 1px solid #ccc;
                            border-radius: 5px;
                            cursor: pointer;
                            font-size: 12px;
                            font-weight: 600;
                        }
                        .tab-btn.active {
                            background: #667eea;
                            color: white;
                            border-color: #667eea;
                        }
                        .tab-content {
                            display: none;
                        }
                        .tab-content.show {
                            display: block;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1>📊 Code Analyzer</h1>
                        <p class="subtitle">Upload Java source code and analyze call graphs to specific dependencies</p>
                        
                        <form id="analyzerForm">
                            <div class="form-group">
                                <label for="sources">Source Code (ZIP file)</label>
                                <input type="file" id="sources" name="sources" accept=".zip" required>
                                <p class="info-text">Upload a ZIP file containing Java source code (.java files)</p>
                            </div>

                            <div class="form-group">
                                <label for="mainClass">Main Class</label>
                                <input type="text" id="mainClass" name="mainClass" placeholder="e.g., com.example.Main" required>
                                <p class="info-text">Fully qualified class name with main(String[] args) method</p>
                            </div>

                            <div class="form-group">
                                <label for="depPrefix">Dependency Target (Package Prefix)</label>
                                <input type="text" id="depPrefix" name="depPrefix" placeholder="e.g., org.apache.poi" required>
                                <p class="info-text">Package prefix of the dependency you want to analyze (e.g., org.apache.poi, java.io)</p>
                            </div>

                            <div class="form-group">
                                <label for="libs">Dependencies (ZIP file) - Optional</label>
                                <input type="file" id="libs" name="libs" accept=".zip">
                                <p class="info-text">Upload a ZIP file containing JAR dependencies if needed</p>
                            </div>

                            <button type="submit">Analyze Code</button>
                        </form>

                        <div class="loading" id="loading">
                            <div class="spinner"></div>
                            <p style="margin-top: 10px; color: #666;">Analyzing code... This may take a few moments.</p>
                        </div>

                        <div id="result">
                            <div class="tab-buttons">
                                <button type="button" class="tab-btn active" onclick="showTab('output')">Analysis Output</button>
                                <button type="button" class="tab-btn" onclick="showTab('json')">JSON Result</button>
                            </div>

                            <div id="output" class="tab-content show"></div>
                            <div id="json" class="tab-content"></div>
                        </div>
                    </div>

                    <script>
                        function showTab(tab) {
                            document.querySelectorAll('.tab-content').forEach(el => el.classList.remove('show'));
                            document.querySelectorAll('.tab-btn').forEach(el => el.classList.remove('active'));
                            document.getElementById(tab).classList.add('show');
                            event.target.classList.add('active');
                        }

                        document.getElementById('analyzerForm').addEventListener('submit', async (e) => {
                            e.preventDefault();
                            
                            const formData = new FormData();
                            formData.append('sources', document.getElementById('sources').files[0]);
                            formData.append('mainClass', document.getElementById('mainClass').value);
                            formData.append('depPrefix', document.getElementById('depPrefix').value);
                            
                            if (document.getElementById('libs').files[0]) {
                                formData.append('libs', document.getElementById('libs').files[0]);
                            }

                            document.getElementById('loading').classList.add('show');
                            document.getElementById('result').classList.remove('show');

                            try {
                                const response = await fetch('/analyze', {
                                    method: 'POST',
                                    body: formData
                                });

                                const data = await response.json();
                                document.getElementById('loading').classList.remove('show');

                                if (response.ok || data.status === 'success') {
                                    showAnalysisResult(data);
                                } else {
                                    showError(data.error || 'Analysis failed');
                                }
                            } catch (error) {
                                document.getElementById('loading').classList.remove('show');
                                showError('Error: ' + error.message);
                            }
                        });

                        function showAnalysisResult(data) {
                            const outputDiv = document.getElementById('output');
                            const jsonDiv = document.getElementById('json');
                            
                            outputDiv.innerHTML = '<pre>' + escapeHtml(data.console_output) + '</pre>';
                            
                            if (data.analysis) {
                                jsonDiv.innerHTML = '<pre>' + JSON.stringify(data.analysis, null, 2) + '</pre>';
                            } else {
                                jsonDiv.innerHTML = '<p style="color: #999;">No analysis data available</p>';
                            }

                            document.getElementById('result').classList.add('show');
                        }

                        function showError(message) {
                            document.getElementById('result').innerHTML = '<div class="error">' + escapeHtml(message) + '</div>';
                            document.getElementById('result').classList.add('show');
                        }

                        function escapeHtml(text) {
                            const div = document.createElement('div');
                            div.textContent = text;
                            return div.innerHTML;
                        }
                    </script>
                </body>
                </html>
                """;
    }
}
