# 🚀 Analyzer Service - Quick Reference Card

## 📡 API Endpoints

### Analyze Project
```bash
curl -X POST http://localhost:8080/api/analyze \
  -H "Content-Type: application/json" \
  -d '{
    "projectId": "abc123",
    "sourceZipUrl": "https://backend.com/project.zip",
    "dependencies": [
      {
        "groupId": "org.springframework.boot",
        "artifactId": "spring-boot-starter-web",
        "version": "3.2.0"
      }
    ]
  }'
```

### Health Check
```bash
curl http://localhost:8080/api/health
```

---

## 🐳 Docker Commands

```bash
# Build
docker build -t analyzer-service:latest .

# Run
docker run -p 8080:8080 \
  -e FASTAPI_BACKEND_URL=http://backend:8000 \
  -v $(pwd)/repository:/app/repository \
  analyzer-service:latest

# Docker Compose
docker-compose up -d          # Start
docker-compose logs -f        # View logs
docker-compose down           # Stop
```

---

## 🛠️ Maven Commands

```bash
mvn clean package             # Build JAR
mvn spring-boot:run          # Run locally
mvn test                      # Run tests
mvn dependency:tree           # Check dependencies
```

---

## 📂 Key Directories

```
/app/repository/{projectId}/
├── src/                      # Extracted source code
├── build/classes/            # Compiled bytecode
└── pom.xml                   # Generated Maven config

/app/logs/
└── analyzer-service.log      # Application logs
```

---

## ⚙️ Environment Variables

```bash
JAVA_OPTS="-Xmx2g -Xms512m"
SPRING_PROFILES_ACTIVE="production"
FASTAPI_BACKEND_URL="http://backend:8000"
ANALYZER_REPOSITORY_BASE="/app/repository"
```

---

## 📊 Analysis Output Structure

```json
{
  "projectId": "abc123",
  "callGraph": {
    "nodes": [
      {"id": "n1", "label": "Main.main()", "type": "entry"},
      {"id": "n2", "label": "Service.process()", "type": "intermediate"},
      {"id": "n3", "label": "VulnLib.execute()", "type": "vulnerable"}
    ],
    "edges": [
      {"source": "n1", "target": "n2"},
      {"source": "n2", "target": "n3"}
    ]
  },
  "cfg": [
    {
      "class": "Service",
      "method": "process()",
      "nodes": [
        {"id": "c1", "stmt": "x = new Object()"},
        {"id": "c2", "stmt": "return x"}
      ],
      "edges": [
        {"source": "c1", "target": "c2"}
      ]
    }
  ]
}
```

---

## 🔍 Node Type Classification

| Type | Description | Example |
|------|-------------|---------|
| `entry` | Main methods, controllers | `main()`, `@RestController` methods |
| `intermediate` | Application code | Service methods, utilities |
| `vulnerable` | Dependency code | Methods from external libraries |

---

## 📝 Configuration (application.properties)

```properties
# Server
server.port=8080

# FastAPI
fastapi.backend.url=http://backend:8000
fastapi.backend.result-endpoint=/api/analysis/result
fastapi.backend.error-endpoint=/api/analysis/error
fastapi.backend.retry-attempts=3

# Paths
analyzer.repository.base=/app/repository

# Async
spring.task.execution.pool.core-size=4
spring.task.execution.pool.max-size=8
```

---

## 🐛 Troubleshooting

### Check Logs
```bash
# Docker
docker logs analyzer-service

# Local file
tail -f /app/logs/analyzer-service.log
```

### Common Issues

**Port already in use:**
```bash
# Change port in application.properties
server.port=8081
```

**Out of memory:**
```bash
# Increase heap size
export JAVA_OPTS="-Xmx4g -Xms1g"
```

**FastAPI connection failed:**
```bash
# Check backend URL
curl http://backend:8000/api/health
```

---

## 📚 Resources

- **Full Documentation**: `README_NEW.md`
- **Migration Guide**: `MIGRATION_SUMMARY.md`
- **GitHub**: https://github.com/Stewella/AutoVulcan-Core
- **Spring Boot Docs**: https://spring.io/projects/spring-boot
- **SootUp**: https://github.com/soot-oss/SootUp

---

## ✅ Health Check Response

```json
{
  "status": "healthy",
  "service": "analyzer-service"
}
```

---

**Version**: 1.0.0  
**Framework**: Spring Boot 3.2.0  
**Java**: 17  
**SootUp**: 2.0.0
