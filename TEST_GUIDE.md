# 📥 Test & Download JSON Results - AutoVulcan Core

## Ringkasan Singkat

**Masalah sebelumnya:** Test Petshop tidak berhasil karena:
1. Path repository di `application.properties` menggunakan Linux path (`/app/repository`) yang tidak cocok untuk Windows
2. Tidak ada cara mudah untuk download hasil JSON ke local

**Solusi:**
1. ✅ Path sudah diperbaiki ke `./repository` (Windows compatible)
2. ✅ Script otomatis `test-and-download-results.ps1` sudah dibuat
3. ✅ JSON hasil akan di-copy otomatis ke folder `analysis-results/`

---

## 🚀 Cara Test (Super Mudah!)

### Step 1: Build Project
```powershell
cd "d:\STEFFANY H\STE_\KULIAH MD\Proyek Penelitian Terapan\autovulcan_core"
mvn clean package -DskipTests
```

### Step 2: Jalankan Test Script
```powershell
.\test-and-download-results.ps1
```

**Itu saja!** Script akan:
- ✅ Start analyzer service otomatis
- ✅ Send request untuk analyze https://github.com/Stewella/Petshop
- ✅ Wait sampai analysis selesai (max 3 menit)
- ✅ **Download JSON hasil ke folder `analysis-results/`**
- ✅ Tampilkan summary hasil analysis
- ✅ Stop service setelah selesai

---

## 📁 Di Mana File JSON Hasil Analysis?

### Lokasi 1: Original (di repository engine)
```
d:\STEFFANY H\STE_\KULIAH MD\Proyek Penelitian Terapan\autovulcan_core\
  └── repository/
      └── petshop_1733728900123/    ← ProjectId yang di-generate
          ├── src/                   ← Source code Petshop
          ├── build/                 ← Compiled classes
          ├── results/
          │   └── analysis-result.json  ← 🎯 FILE JSON DI SINI!
          └── pom.xml
```

### Lokasi 2: Downloaded (di local Anda) - MUDAH AKSES!
```
d:\STEFFANY H\STE_\KULIAH MD\Proyek Penelitian Terapan\autovulcan_core\
  └── analysis-results/
      ├── petshop-analysis-20251209_101530.json  ← 🎯 JSON HASIL (mudah diakses!)
      ├── request-response-20251209_101530.json  ← Response dari API
      └── petshop-20251209_101530/               ← Full project directory (backup)
          ├── src/
          ├── build/
          └── results/
              └── analysis-result.json
```

**Tip:** Buka folder ini untuk lihat hasilnya:
```powershell
code analysis-results\
# atau
explorer analysis-results\
```

---

## 📊 Isi File JSON Result

File `analysis-result.json` berisi hasil static analysis:

```json
{
  "projectId": "petshop_1733728900123",
  "callGraph": {
    "nodes": [
      {
        "id": "node-1",
        "methodSignature": "<org.example.PetController: void addPet(Pet)>",
        "className": "org.example.PetController",
        "methodName": "addPet",
        "isEntryPoint": true
      },
      {
        "id": "node-2",
        "methodSignature": "<org.example.PetService: void save(Pet)>",
        "className": "org.example.PetService",
        "methodName": "save",
        "isEntryPoint": false
      }
    ],
    "edges": [
      {
        "from": "node-1",
        "to": "node-2",
        "callType": "INVOKE_VIRTUAL"
      }
    ]
  },
  "cfg": [
    {
      "methodSignature": "<org.example.PetController: void addPet(Pet)>",
      "nodes": [
        {
          "id": "cfg-node-1",
          "label": "entry",
          "statement": null
        },
        {
          "id": "cfg-node-2",
          "label": "stmt-1",
          "statement": "r0 := @this: org.example.PetController"
        }
      ],
      "edges": [
        {
          "from": "cfg-node-1",
          "to": "cfg-node-2",
          "label": "normal"
        }
      ]
    }
  ]
}
```

### Penjelasan Isi JSON:

- **`projectId`**: ID unik project (auto-generated)
- **`callGraph`**: Graph yang menunjukkan method mana memanggil method mana
  - `nodes`: Semua method dalam aplikasi
  - `edges`: Panggilan antar method
- **`cfg`**: Control Flow Graph untuk setiap method
  - `nodes`: Statement/instruksi dalam method
  - `edges`: Alur eksekusi antar statement

---

## 🔧 Troubleshooting

### ❌ Service tidak start?

**Cek port 8080:**
```powershell
Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue
```

**Kill Java process:**
```powershell
Get-Process java -ErrorAction SilentlyContinue | Stop-Process -Force
```

### ❌ JSON tidak terbuat?

1. **Cek service logs** di terminal
2. **Cek folder repository:**
   ```powershell
   ls repository\
   ls repository\petshop_*\results\
   ```
3. **Cek apakah Petshop bisa di-download:**
   ```powershell
   Invoke-WebRequest "https://github.com/Stewella/Petshop/archive/refs/heads/main.zip" -OutFile "test.zip"
   ```

### ❌ Build gagal?

**Rebuild dengan verbose:**
```powershell
mvn clean package -DskipTests -X
```

---

## 📝 Test Manual (Tanpa Script)

Kalau mau test manual tanpa script:

### 1. Start Service
```powershell
java -Xmx2g -jar target/analyzer-service-1.0.0.jar
```

Tunggu sampai muncul:
```
Started AnalyzerServiceApplication in X seconds
Tomcat started on port 8080
```

### 2. Send Request (Buka Terminal Baru)
```powershell
$payload = @{
    sourceZipUrl = "https://github.com/Stewella/Petshop/archive/refs/heads/main.zip"
    projectId = ""
    dependencies = @()
} | ConvertTo-Json

$response = Invoke-WebRequest -Uri "http://localhost:8080/api/analyze" `
    -Method POST `
    -ContentType "application/json" `
    -Body $payload

$result = $response.Content | ConvertFrom-Json
Write-Host "ProjectId: $($result.projectId)"
```

### 3. Wait & Check Results

Tunggu 2-3 menit untuk analysis selesai, lalu:

```powershell
# Lihat projectId yang terbuat
ls repository\

# Example: petshop_1733728900123
$projectId = "petshop_1733728900123"  # ganti dengan ID yang muncul

# Cek hasil JSON
cat "repository\$projectId\results\analysis-result.json"

# Copy ke local
Copy-Item "repository\$projectId\results\analysis-result.json" `
          -Destination "my-petshop-result.json"
```

### 4. View JSON

```powershell
# Pretty print
Get-Content my-petshop-result.json | ConvertFrom-Json | ConvertTo-Json -Depth 10

# Open in VS Code
code my-petshop-result.json
```

---

## 📚 Dokumentasi Lainnya

- **ARCHITECTURE.md** - Penjelasan lengkap arsitektur sistem
- **PROJECTID_GUIDE.md** - Cara kerja auto-generate projectId
- **QUICK_REFERENCE.md** - Command-command penting
- **README.md** - Overview project

---

## ✅ Checklist

Sebelum test, pastikan:

- [ ] Build sudah success (`mvn clean package -DskipTests`)
- [ ] JAR ada di `target/analyzer-service-1.0.0.jar`
- [ ] Port 8080 tidak dipakai process lain
- [ ] Internet connection OK (untuk download Petshop repo)

**Siap test!** 🚀

```powershell
.\test-and-download-results.ps1
```
