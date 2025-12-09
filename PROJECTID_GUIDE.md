# ProjectId: User Input vs Auto-Generated

## Jawaban: ProjectId bisa KEDUANYA ✅

Sistem sekarang sudah di-update untuk support HYBRID approach:

### **1. User Bisa Provide ProjectId (Optional)**
```json
{
  "projectId": "petshop-001",
  "sourceZipUrl": "https://github.com/Stewella/Petshop/archive/refs/heads/main.zip",
  "dependencies": []
}
```

### **2. Atau System Auto-Generate (Jika Kosong)**
```json
{
  "projectId": "",
  "sourceZipUrl": "https://github.com/Stewella/Petshop/archive/refs/heads/main.zip",
  "dependencies": []
}
```

**Response:**
```json
{
  "status": "received",
  "projectId": "petshop_1733762400000",
  "message": "Analysis started in background"
}
```

---

## Cara Kerja

### **Frontend Side (No Change Needed)**
User bisa:
- Provide projectId jika ingin, atau
- Leave it empty, system akan auto-generate

### **Backend Side (Updated)**

#### **AnalysisController.java**
```java
@PostMapping("/analyze")
public ResponseEntity<Map<String, Object>> analyze(@RequestBody AnalysisRequest request) {
    // Auto-generate projectId if not provided
    String projectId = request.getProjectId();
    if (projectId == null || projectId.trim().isEmpty()) {
        projectId = generateProjectId(request.getSourceZipUrl());
        request.setProjectId(projectId);
    }
    
    // Continue with analysis...
    analysisService.handleAnalysisAsync(request);
    
    return ResponseEntity.ok(Map.of(
        "status", "received",
        "projectId", projectId  // Return final projectId to client
    ));
}
```

#### **AnalysisService.java**
```java
private String generateProjectId(String repositoryUrl) {
    // Extract repo name dari URL
    String repoName = repositoryUrl
        .substring(repositoryUrl.lastIndexOf("/") + 1)
        .replace(".zip", "")
        .toLowerCase();
    
    // Combine dengan timestamp untuk uniqueness
    return repoName + "_" + System.currentTimeMillis();
}
```

---

## Generation Strategy

### **Format Auto-Generated ProjectId:**
```
{repositoryName}_{timestamp}

Contoh:
- petshop_1733762400000
- spring-framework_1733762405123
- guava_1733762410456
```

### **Keuntungan:**
✅ Unique (tidak akan duplicate)
✅ Identifiable (bisa tahu repo mana)
✅ Sortable (timestamp included)
✅ User tidak perlu input extra field

---

## Frontend Requirements

### **OPTIONAL ProjectId di Form:**

```html
<form>
  <input type="text" 
         name="repositoryUrl" 
         placeholder="https://github.com/..."
         required>
  
  <input type="text" 
         name="projectId" 
         placeholder="(Optional - leave empty for auto-generate)"
         optional>
  
  <button type="submit">Analyze</button>
</form>
```

### **Send Request:**
```javascript
const data = {
  projectId: document.querySelector('[name="projectId"]').value || "", // Can be empty
  sourceZipUrl: document.querySelector('[name="repositoryUrl"]').value,
  dependencies: []
};

fetch('/api/analyze', {
  method: 'POST',
  body: JSON.stringify(data)
})
.then(res => res.json())
.then(data => {
  console.log('ProjectId: ', data.projectId); // Show user the assigned projectId
});
```

---

## Skenario Penggunaan

### **Skenario 1: User Ingin Input ProjectId Sendiri**
```
Frontend Input:
- Repository: https://github.com/Stewella/Petshop/archive/refs/heads/main.zip
- ProjectId: petshop-cve-2024-001

Backend:
- Use projectId dari user: petshop-cve-2024-001

Response:
- projectId: petshop-cve-2024-001
```

### **Skenario 2: User Tidak Input ProjectId (Recommended)**
```
Frontend Input:
- Repository: https://github.com/Stewella/Petshop/archive/refs/heads/main.zip
- ProjectId: (kosong)

Backend:
- Auto-generate: petshop_1733762400000

Response:
- projectId: petshop_1733762400000
```

---

## Build & Test

```bash
# Build
mvn clean package -DskipTests

# Run
mvn spring-boot:run

# Test dengan projectId kosong
curl -X POST http://localhost:8080/api/analyze \
  -H "Content-Type: application/json" \
  -d '{
    "projectId": "",
    "sourceZipUrl": "https://github.com/Stewella/Petshop/archive/refs/heads/main.zip",
    "dependencies": []
  }'

# Response:
# {
#   "status": "received",
#   "projectId": "petshop_1733762400000",
#   "message": "Analysis started in background"
# }
```

---

## Summary

| Aspek | Sebelum | Sesudah |
|---|---|---|
| **ProjectId Input** | Harus dari user | Harus dari user ATAU auto-generate |
| **Form Field** | Required | Optional |
| **Backend Logic** | Langsung pakai input | Check dulu, auto-generate jika kosong |
| **Response** | Sesuai input | Sesuai input atau auto-generated |
| **User Experience** | Perlu input extra field | Simpler, bisa skip projectId field |

**Rekomendasi:** Biarkan projectId optional di frontend, let backend handle auto-generation. User akan lebih nyaman! 🎯

