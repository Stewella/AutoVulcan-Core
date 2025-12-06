# Analyzer Service Architecture

## Overview

The Analyzer Service is a Spring Boot microservice that performs static analysis on Java projects to identify potential vulnerabilities by analyzing call graphs and control flow patterns. It uses SootUp for bytecode analysis and integrates with a FastAPI backend for further processing.

## System Architecture

```
┌─────────────────┐
│   REST Client   │
│  (External)     │
└────────┬────────┘
         │ HTTP POST /api/analyze
         ▼
┌─────────────────────────────────────────────────┐
│           Analyzer Service (Port 8090)          │
│  ┌───────────────────────────────────────────┐  │
│  │     AnalysisController (REST API)         │  │
│  │  - POST /api/analyze                      │  │
│  │  - GET  /api/health                       │  │
│  └─────────────────┬─────────────────────────┘  │
│                    │                             │
│  ┌─────────────────▼─────────────────────────┐  │
│  │       AnalysisService (Orchestrator)      │  │
│  │  - Download repository                    │  │
│  │  - Generate POM                           │  │
│  │  - Compile project                        │  │
│  │  - Trigger analysis                       │  │
│  └──┬───────────────────────────┬────────────┘  │
│     │                           │                │
│  ┌──▼───────────┐        ┌──────▼──────────┐    │
│  │ SootUpEngine │        │  FastApiClient  │    │
│  │ (Analyzer)   │        │  (HTTP Client)  │    │
│  └──┬───────────┘        └──────┬──────────┘    │
│     │                           │                │
│  ┌──▼───────────┐        ┌──────▼──────────┐    │
│  │ Call Graph   │        │  Backend API    │    │
│  │ + CFG Data   │        │  (FastAPI)      │    │
│  └──────────────┘        └─────────────────┘    │
└─────────────────────────────────────────────────┘
```

## Core Components

### 1. **AnalysisController** (`controller/AnalysisController.java`)

**Purpose**: REST API entry point for analysis requests

**Endpoints**:
- `POST /api/analyze` - Submit analysis request
- `GET /api/health` - Service health check

**Request Format**:
```json
{
  "repositoryUrl": "https://github.com/user/repo/archive/refs/heads/main.zip",
  "projectId": "project-123",
  "dependencies": [
    {
      "groupId": "org.apache.logging.log4j",
      "artifactId": "log4j-core",
      "version": "2.14.1"
    }
  ]
}
```

**Response Format**:
```json
{
  "projectId": "project-123",
  "callGraph": {
    "nodes": [...],
    "edges": [...]
  },
  "cfg": [...]
}
```

### 2. **AnalysisService** (`service/AnalysisService.java`)

**Purpose**: Orchestrates the entire analysis workflow

**Workflow**:
1. **Download Phase**: Downloads repository archive to temp directory
2. **Preparation Phase**: Generates dynamic `pom.xml` based on dependencies
3. **Compilation Phase**: Executes Maven compile to generate bytecode
4. **Analysis Phase**: Triggers SootUp static analysis
5. **Integration Phase**: Sends results to FastAPI backend (async)
6. **Cleanup Phase**: Removes temporary files

**Key Methods**:
- `analyzeProject(request)` - Main entry point for analysis
- Delegates to specialized utility classes for each phase

### 3. **SootUpEngine** (`service/SootUpEngine.java`)

**Purpose**: Performs static analysis using SootUp framework

**Analysis Steps**:

#### Step 1: Load Bytecode
```java
JavaClassPathAnalysisInputLocation inputLocation = 
    new JavaClassPathAnalysisInputLocation(classesDir.toString());
JavaView view = new JavaView(Collections.singletonList(inputLocation));
```
Loads compiled `.class` files into SootUp's memory model.

#### Step 2: Find Entry Points
```java
List<MethodSignature> entryPoints = findEntryPoints(view);
```
Identifies starting points for analysis:
- Methods named `main` (static, public)
- Spring-related methods (controllers, handlers)
- If none found, uses first available method

#### Step 3: Build Call Graph
```java
CallGraphAlgorithm cha = new ClassHierarchyAnalysisAlgorithm(view);
CallGraph cg = cha.initialize(entryPoints);
```
Uses **Class Hierarchy Analysis (CHA)** to build method call relationships.

#### Step 4: Extract Call Graph
Iterates through all calls and creates nodes/edges:
- **Node Types**:
  - `entry`: Entry point methods (main, controllers)
  - `vulnerable`: Methods from dependency packages
  - `intermediate`: Other methods
- **Edges**: Represent method-to-method calls

#### Step 5: Extract Control Flow Graphs (CFG)
For each method, extracts Jimple statements and builds CFG:
- **Nodes**: Individual statements
- **Edges**: Control flow between statements

**Output**:
- Call graph with classified nodes
- CFG for each method in the project

### 4. **FastApiClient** (`client/FastApiClient.java`)

**Purpose**: Async HTTP client for backend communication

**Features**:
- **Async Execution**: Uses `@Async` annotation
- **Retry Logic**: Up to 3 attempts with exponential backoff
- **Connection Pooling**: OkHttp client with configured timeouts
- **Error Handling**: Logs failures and rethrows exceptions

**Configuration**:
```java
private static final int MAX_RETRIES = 3;
private static final long INITIAL_BACKOFF_MS = 1000;
connectTimeout: 30s
readTimeout: 60s
writeTimeout: 60s
```

### 5. **Supporting Utilities**

#### **PomGenerator** (`service/PomGenerator.java`)
Generates dynamic `pom.xml` files:
- Java 17 configuration
- Maven compiler plugin
- Custom dependencies from request
- Standard Spring Boot structure

#### **MavenExecutor** (`service/MavenExecutor.java`)
Executes Maven commands:
- Runs `mvn clean compile`
- Captures output and errors
- Validates compilation success

#### **DownloadUtil** (`service/DownloadUtil.java`)
Downloads and extracts repositories:
- Downloads ZIP archives
- Extracts to temporary directory
- Supports nested project structures

## Data Models

### **AnalysisRequest**
```java
- String repositoryUrl
- String projectId
- List<Dependency> dependencies
```

### **Dependency**
```java
- String groupId
- String artifactId
- String version
```

### **CallGraph**
```java
- List<CallGraphNode> nodes
- List<CallGraphEdge> edges
```

### **CallGraphNode**
```java
- String id
- String label (fully qualified method name)
- String type (entry/vulnerable/intermediate)
```

### **CallGraphEdge**
```java
- String source (node id)
- String target (node id)
```

### **CFGMethod**
```java
- String className
- String method
- List<CFGNode> nodes
- List<CFGEdge> edges
```

### **AnalysisResult**
```java
- String projectId
- CallGraph callGraph
- List<CFGMethod> cfg
```

## Configuration

### **application.properties**
```properties
# Server
server.port=8080

# Async configuration
spring.task.execution.pool.core-size=5
spring.task.execution.pool.max-size=10
spring.task.execution.pool.queue-capacity=100

# Logging
logging.level.com.example.analyzer=INFO

# FastAPI backend URL
fastapi.backend.url=http://backend-fastapi:8000
```

### **Docker Configuration**
- **Internal Port**: 8080
- **External Port**: 8090 (docker-compose)
- **Memory**: 2GB max, 512MB initial
- **Volumes**:
  - `./repository:/app/repository` - Temp analysis workspace
  - `./logs:/app/logs` - Application logs

## Analysis Flow Details

### Phase 1: Request Reception
```
Client → POST /api/analyze → AnalysisController
       ↓
   Validates request (repo URL, dependencies)
       ↓
   Returns 202 Accepted (analysis starts async)
```

### Phase 2: Repository Download
```
AnalysisService → DownloadUtil
       ↓
   Download ZIP from URL
       ↓
   Extract to /tmp/analysis-{projectId}
       ↓
   Detect project root (contains src/)
```

### Phase 3: Project Setup
```
PomGenerator
       ↓
   Generate pom.xml with:
   - Java 17
   - Maven compiler 3.11.0
   - User-specified dependencies
       ↓
   Write to project root
```

### Phase 4: Compilation
```
MavenExecutor
       ↓
   Execute: mvn clean compile
       ↓
   Check exit code
       ↓
   Verify target/classes exists
```

### Phase 5: Static Analysis
```
SootUpEngine
       ↓
   Load bytecode → JavaView
       ↓
   Find entry points
       ↓
   Build call graph (CHA algorithm)
       ↓
   Extract CFG for each method
       ↓
   Classify nodes based on dependencies
       ↓
   Return AnalysisResult
```

### Phase 6: Backend Integration
```
FastApiClient (async)
       ↓
   Retry loop (max 3 attempts)
       ↓
   POST results to FastAPI backend
       ↓
   Log success/failure
```

### Phase 7: Cleanup
```
AnalysisService
       ↓
   Delete temp directories
       ↓
   Delete generated POM files
       ↓
   Free resources
```

## Vulnerability Detection

The service identifies vulnerable code paths by:

1. **Dependency Tracking**: Stores package names from dependency list
2. **Node Classification**: Marks methods from dependency packages as "vulnerable"
3. **Call Path Analysis**: Traces calls from entry points to vulnerable methods
4. **CFG Extraction**: Provides detailed control flow within each method

**Example**:
If analyzing a project with `log4j-core:2.14.1` dependency:
- Any method in `org.apache.logging.log4j.*` packages → `vulnerable` node
- Methods calling these → tracked via call graph edges
- Entry points → show how application reaches vulnerable code

## Error Handling

### Compilation Errors
- **Action**: Log error output and throw `RuntimeException`
- **Client**: Receives 500 with error message

### Analysis Errors
- **Action**: Log stack trace and return partial results if possible
- **Client**: Receives 500 or incomplete data

### Backend Communication Errors
- **Action**: Retry up to 3 times, then log failure
- **Impact**: Analysis completes but results not sent to backend

### Download Errors
- **Action**: Throw exception immediately
- **Client**: Receives 500 with network error details

## Performance Considerations

- **Async Processing**: Analysis runs in background thread pool
- **Memory**: 2GB heap for large projects
- **Compilation Time**: Depends on project size (30s - 5min typical)
- **Analysis Time**: Proportional to number of methods (1-10min typical)
- **Cleanup**: Automatic temp file deletion after analysis

## Extension Points

### Adding New Analysis Types
1. Create new method in `SootUpEngine`
2. Update `AnalysisResult` model with new fields
3. Modify `AnalysisService` to call new analysis
4. Update API documentation

### Custom Entry Point Detection
Modify `SootUpEngine.findEntryPoints()`:
```java
// Add custom annotation or pattern matching
if (method.hasAnnotation("CustomEntryPoint")) {
    entryPoints.add(method.getSignature());
}
```

### Additional Vulnerability Patterns
Enhance `SootUpEngine.isVulnerableMethod()`:
```java
// Add pattern-based detection
if (className.contains("Unsafe") || methodName.contains("execute")) {
    return true;
}
```

## Monitoring & Debugging

### Health Check
```bash
curl http://localhost:8090/api/health
```

### Logs
- **Location**: `./logs/analyzer-service.log`
- **Format**: Timestamp, level, class, message
- **Levels**: INFO (default), DEBUG (verbose), ERROR

### Docker Logs
```bash
docker-compose logs -f analyzer-service
```

### Common Issues

**Problem**: Compilation fails
- **Check**: Dependencies are correct and accessible
- **Check**: Maven repository connectivity
- **Debug**: Review Maven output in logs

**Problem**: No entry points found
- **Check**: Project contains main methods or controllers
- **Workaround**: Service falls back to first available method

**Problem**: Analysis timeout
- **Check**: Project size (very large projects may need more time)
- **Solution**: Increase heap memory or add timeout configuration

## Security Considerations

- **Temp Files**: Cleaned up after analysis (no persistent data)
- **Input Validation**: Repository URLs validated before download
- **Resource Limits**: Memory and CPU constrained via Docker
- **Network Isolation**: Runs in Docker network (not exposed to internet)

## Future Enhancements

- [ ] Support for multi-module Maven projects
- [ ] Gradle build system support
- [ ] Incremental analysis (cache results)
- [ ] Custom vulnerability rule definitions
- [ ] Real-time progress updates via WebSocket
- [ ] Support for other JVM languages (Kotlin, Scala)
