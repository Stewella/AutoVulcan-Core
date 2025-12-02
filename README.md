# Analyzer Service - Java Static Analysis with SootUp

A production-ready Java-based static analysis service that communicates with FastAPI backend to perform deep code analysis using SootUp framework.

## Architecture Overview

```
FastAPI Backend → Analyzer Service (Spring Boot) → SootUp Engine → Analysis Results → FastAPI Backend
```

### Components

1. **Controller Layer** (`AnalysisController`)
   - REST API endpoint `/api/analyze`
   - Health check endpoint `/api/health`

2. **Service Layer**
   - `AnalysisService`: Orchestrates the analysis workflow
   - `SootUpEngine`: Performs static analysis using SootUp
   - `DownloadUtil`: Downloads and extracts source ZIP files
   - `PomGenerator`: Generates Maven pom.xml from dependency list
   - `MavenExecutor`: Compiles projects using Maven

3. **Client Layer**
   - `FastApiClient`: Async HTTP client with retry logic for backend communication

4. **Model Layer**
   - Request/Response models with Jackson serialization
   - Call graph and CFG data structures

## API Specification

### Input (from FastAPI)

**POST** `http://analyzer-service:8080/api/analyze`

```json
{
  "projectId": "abc123",
  "sourceZipUrl": "https://backend.com/uploads/projects/abc123.zip",
  "dependencies": [
    {
      "groupId": "org.springframework.boot",
      "artifactId": "spring-boot-starter-webflux",
      "version": "3.2.0"
    }
  ]
}
```

### Output (to FastAPI)

**POST** `http://backend-fastapi:8000/api/analysis/result`

```json
{
  "projectId": "abc123",
  "analysis": {
    "callGraph": {
      "nodes": [
        {"id": "n1", "label": "App.main()", "type": "entry"},
        {"id": "n2", "label": "ServiceA.init()", "type": "intermediate"},
        {"id": "n3", "label": "UnsafeLib.execute()", "type": "vulnerable"}
      ],
      "edges": [
        {"source": "n1", "target": "n2"},
        {"source": "n2", "target": "n3"}
      ]
    },
    "cfg": [
      {
        "class": "ServiceA",
        "method": "init()",
        "nodes": [
          {"id": "c1", "stmt": "new Object()"},
          {"id": "c2", "stmt": "return"}
        ],
        "edges": [
          {"source": "c1", "target": "c2"}
        ]
      }
    ]
  }
}
```

## Analysis Features

### Call Graph Analysis
- **Entry Points**: Identifies main() methods and controller endpoints
- **CHA Algorithm**: Class Hierarchy Analysis for call graph construction
- **Node Classification**:
  - `entry`: Main methods or endpoint handlers
  - `intermediate`: Application code
  - `vulnerable`: Methods from listed dependencies

### Control Flow Graph (CFG)
- Extracts Jimple IR (intermediate representation)
- Builds CFG nodes and edges for each method
- Captures statement-level control flow

## Project Structure

```
analyzer-service/
├── src/main/java/com/example/analyzer/
│   ├── AnalyzerServiceApplication.java
│   ├── controller/
│   │   └── AnalysisController.java
│   ├── service/
│   │   ├── AnalysisService.java
│   │   ├── SootUpEngine.java
│   │   ├── DownloadUtil.java
│   │   ├── PomGenerator.java
│   │   └── MavenExecutor.java
│   ├── client/
│   │   └── FastApiClient.java
│   └── model/
│       ├── AnalysisRequest.java
│       ├── AnalysisResult.java
│       ├── CallGraph.java
│       ├── CallGraphNode.java
│       ├── CallGraphEdge.java
│       ├── CFGMethod.java
│       ├── CFGNode.java
│       ├── CFGEdge.java
│       └── Dependency.java
├── src/main/resources/
│   └── application.properties
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```

## Configuration

Edit `src/main/resources/application.properties`:

```properties
# Server
server.port=8080

# Analyzer paths
analyzer.repository.base=/app/repository

# FastAPI Backend
fastapi.backend.url=http://backend-fastapi.com
fastapi.backend.result-endpoint=/api/analysis/result
fastapi.backend.error-endpoint=/api/analysis/error
fastapi.backend.retry-attempts=3
```

## Building & Running

### Local Development

```bash
# Build
mvn clean package

# Run
java -jar target/analyzer-service-1.0.0.jar
```

### Docker

```bash
# Build image
docker build -t analyzer-service:latest .

# Run container
docker run -p 8080:8080 \
  -e FASTAPI_BACKEND_URL=http://backend:8000 \
  -v $(pwd)/repository:/app/repository \
  analyzer-service:latest
```

### Docker Compose

```bash
# Start service
docker-compose up -d

# View logs
docker-compose logs -f

# Stop service
docker-compose down
```

## Workflow

1. **Receive Request**: FastAPI sends analysis request with project ZIP URL
2. **Download**: Service downloads and extracts source code to `/app/repository/{projectId}/src/`
3. **Generate POM**: Creates `pom.xml` with specified dependencies
4. **Compile**: Runs Maven to compile code to `/app/repository/{projectId}/build/classes/`
5. **Analyze**: SootUp loads bytecode, builds call graph and CFG
6. **Send Result**: Asynchronously posts results back to FastAPI with retry logic

## Dependencies

- **Spring Boot 3.2.0**: Web framework
- **SootUp 2.0.0**: Static analysis engine
- **OkHttp 4.12.0**: Async HTTP client
- **Apache Commons**: File operations
- **Jackson**: JSON serialization
- **Lombok**: Code generation

## Error Handling

- Automatic retries (configurable, default: 3 attempts)
- Exponential backoff between retries
- Error notifications sent to FastAPI on failure
- Comprehensive logging at all stages

## Logging

- Console and file logging
- Log file: `/app/logs/analyzer-service.log`
- Debug level for analyzer components
- Warn level for SootUp (reduces noise)

## Health Check

**GET** `http://localhost:8080/api/health`

```json
{
  "status": "healthy",
  "service": "analyzer-service"
}
```

## Environment Variables

- `JAVA_OPTS`: JVM options (default: `-Xmx2g -Xms512m`)
- `SPRING_PROFILES_ACTIVE`: Spring profile
- `FASTAPI_BACKEND_URL`: Backend URL
- `ANALYZER_REPOSITORY_BASE`: Base path for project repositories

## Production Deployment

1. Configure backend URL in environment
2. Mount persistent volumes for `/app/repository` and `/app/logs`
3. Set appropriate memory limits via `JAVA_OPTS`
4. Use health check endpoint for load balancer
5. Monitor logs for errors and performance

## License

MIT License
