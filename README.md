# SootUp Java Code Analyzer

Alat analisis kode Java yang menggunakan SootUp untuk membangun call graph, menganalisis aliran kontrol, dan melacak dependency.

## Varian Analyzer

### 1. **Analyzer** (Versi Standar)
Menganalisis semua method dan call graph dari kode sumber.

**Penggunaan:**
```bash
java -jar target/SootUpAnalysis-1.0.0-jar-with-dependencies.jar <source-folder> <main-class>
```

**Contoh:**
```bash
# Menganalisis code di folder external_code dengan main class com.example.Main
java -jar target/SootUpAnalysis-1.0.0-jar-with-dependencies.jar external_code com.example.Main

# Menganalisis code di path absolut
java -jar target/SootUpAnalysis-1.0.0-jar-with-dependencies.jar D:\MyProject\src org.myapp.Application
```

**Output:**
- `output/analysis_v8.json` - JSON dengan:
  - `call_graph`: 9 edges dengan source, target, dan line number
  - `methods`: Detail semua method dengan Jimple representation
  - `dependencies`: Peta dependencies antar class

---

### 2. **AnalyzerWithDependencies** 
Analyzer yang mendukung external dependency JARs untuk project yang membutuhkan library eksternal.

**Penggunaan:**
```bash
java -cp target/SootUpAnalysis-1.0.0-jar-with-dependencies.jar org.example.sootup.AnalyzerWithDependencies <source-folder> <libs-folder> <main-class>
```

**Contoh:**
```bash
# Menganalisis project Spring Boot dengan dependencies di folder libs/
java -cp target/SootUpAnalysis-1.0.0-jar-with-dependencies.jar org.example.sootup.AnalyzerWithDependencies \
    D:\MySpringProject\src \
    D:\MySpringProject\target\dependency \
    com.myapp.Application

# Menganalisis project local dengan dependencies
java -cp target/SootUpAnalysis-1.0.0-jar-with-dependencies.jar org.example.sootup.AnalyzerWithDependencies \
    src/main/java \
    libs \
    org.example.Main
```

**Output:**
Sama seperti Analyzer standar, dengan support untuk dependency eksternal.

---

### 3. **AnalyzerWithFilter** (RECOMMENDED)
Analyzer yang paling fleksibel - mendukung filtering berdasarkan package dependency target.

**Penggunaan:**
```bash
java -cp target/SootUpAnalysis-1.0.0-jar-with-dependencies.jar org.example.sootup.AnalyzerWithFilter \
    <source-folder> \
    <main-class> \
    [target-dependency-package] \
    [libs-folder]
```

**Parameter:**
- `source-folder` (wajib): Path ke source code Java
- `main-class` (wajib): Fully qualified main class (e.g., `com.example.Main`)
- `target-dependency-package` (opsional): Package filter (e.g., `org.apache.poi`, `java.io`, `java.lang`)
- `libs-folder` (opsional): Path ke folder dependencies, default: `libs`

**Contoh:**

#### Contoh 1: Tanpa filter (analisis semua)
```bash
java -cp target/SootUpAnalysis-1.0.0-jar-with-dependencies.jar org.example.sootup.AnalyzerWithFilter \
    external_code com.example.Main
```

Output:
```
=== Analyzer With Dependency Filter ===
Source folder        : external_code
Main class           : com.example.Main
Target dependency    : (none - all calls will be analyzed)
...
✔ CallGraph built. Total calls: 9
✔ CallGraph extracted: 9 total edges
```

#### Contoh 2: Filter dengan java.io (untuk System.out.println, PrintStream, dll)
```bash
java -cp target/SootUpAnalysis-1.0.0-jar-with-dependencies.jar org.example.sootup.AnalyzerWithFilter \
    external_code com.example.Main java.io
```

Output:
```
=== Analyzer With Dependency Filter ===
Source folder        : external_code
Main class           : com.example.Main
Target dependency    : java.io
...
✔ CallGraph built. Total calls: 9
✔ CallGraph extracted: 9 total edges
✔ Filtered calls with dependency 'java.io': 3 edges
   - Calls TO target dependency: 3
   - Calls FROM target dependency: 0
✔ Extracted 2 methods from affected classes
✔ JSON exported to output/analysis_v8.json
```

#### Contoh 3: Filter dengan java.lang (untuk exception handling, dll)
```bash
java -cp target/SootUpAnalysis-1.0.0-jar-with-dependencies.jar org.example.sootup.AnalyzerWithFilter \
    external_code com.example.Main java.lang
```

Output:
```
✔ CallGraph built. Total calls: 9
✔ CallGraph extracted: 9 total edges
✔ Filtered calls with dependency 'java.lang': 2 edges
   - Calls TO target dependency: 2
   - Calls FROM target dependency: 0
✔ Extracted 4 methods from affected classes
```

#### Contoh 4: Filter dengan Apache POI (untuk Excel operations)
```bash
java -cp target/SootUpAnalysis-1.0.0-jar-with-dependencies.jar org.example.sootup.AnalyzerWithFilter \
    external_code com.myapp.ExcelProcessor org.apache.poi ./libs
```

#### Contoh 5: Filter dengan Spring Framework
```bash
java -cp target/SootUpAnalysis-1.0.0-jar-with-dependencies.jar org.example.sootup.AnalyzerWithFilter \
    src/main/java com.myapp.Application org.springframework ./target/dependency
```

---

## Output JSON Structure

### Tanpa Filter:
```json
{
  "metadata": {
    "source_folder": "external_code",
    "main_class": "com.example.Main",
    "target_dependency": "none",
    "total_calls": 9,
    "filtered_calls": 9
  },
  "call_graph": [
    {
      "src": "<com.example.Main: void main(java.lang.String[])>",
      "tgt": "<com.example.Calculator: int add(int,int)>",
      "line": 8,
      "src_package": "com.example",
      "tgt_package": "com.example",
      "matches": []
    },
    ...
  ],
  "methods": [...],
  "dependencies": {...}
}
```

### Dengan Filter (target_dependency: java.io):
```json
{
  "metadata": {
    "source_folder": "external_code",
    "main_class": "com.example.Main",
    "target_dependency": "java.io",
    "total_calls": 9,
    "filtered_calls": 3
  },
  "call_graph": [
    {
      "src": "<com.example.Main: void main(java.lang.String[])>",
      "tgt": "<java.io.PrintStream: void println(java.lang.String)>",
      "line": 9,
      "src_package": "com.example",
      "tgt_package": "java.io",
      "matches": ["TARGET_MATCHES"]  // Indicates target dependency matched
    },
    ...
  ],
  "methods": [
    {
      "class": "com.example.Main",
      "method": "<com.example.Main: void main(java.lang.String[])>",
      "package": "com.example",
      "jimple": [
        "l0 := @parameter0: java.lang.String[]",
        "$stack6 = <java.lang.System: java.io.PrintStream out>",
        "virtualinvoke $stack6.<java.io.PrintStream: void println(java.lang.String)>($stack7)",
        ...
      ]
    }
  ],
  "dependencies": {
    "com.example.Main": ["java.io.PrintStream", "com.example.Calculator"],
    "com.example.Calculator": ["java.lang.ArithmeticException", "java.lang.Object"]
  }
}
```

---

## Filter Matching Types

Dalam JSON output, field `matches` menunjukkan tipe filter yang cocok:

- **`SOURCE_MATCHES`**: Kode sumber (caller) berasal dari target dependency
- **`TARGET_MATCHES`**: Kode target (callee) adalah bagian dari target dependency
- **`[]` (empty)**: Tidak ada filter atau filter tidak diaktifkan

---

## Common Use Cases

### 1. Analisis Penggunaan Library Tertentu
```bash
# Lihat semua kode yang menggunakan Apache POI
java -cp target/SootUpAnalysis-1.0.0-jar-with-dependencies.jar org.example.sootup.AnalyzerWithFilter \
    src/main/java com.myapp.ExcelExporter org.apache.poi ./libs
```

### 2. Security Analysis - Tracking Input/Output
```bash
# Lihat semua call ke Java I/O (file read/write)
java -cp target/SootUpAnalysis-1.0.0-jar-with-dependencies.jar org.example.sootup.AnalyzerWithFilter \
    src/main/java com.myapp.DataProcessor java.io

# Lihat semua call ke Java Reflection (potential security risk)
java -cp target/SootUpAnalysis-1.0.0-jar-with-dependencies.jar org.example.sootup.AnalyzerWithFilter \
    src/main/java com.myapp.Application java.lang.reflect
```

### 3. CVE Analysis - Tracking Vulnerable Library Usage
```bash
# Lihat bagaimana kode menggunakan library yang rentan
java -cp target/SootUpAnalysis-1.0.0-jar-with-dependencies.jar org.example.sootup.AnalyzerWithFilter \
    src/main/java com.myapp.Application org.apache.struts ./libs

# Lihat penggunaan Log4j (untuk CVE-2021-44228)
java -cp target/SootUpAnalysis-1.0.0-jar-with-dependencies.jar org.example.sootup.AnalyzerWithFilter \
    src/main/java com.myapp.Logger org.apache.logging.log4j
```

### 4. Database Operation Tracking
```bash
# Lihat semua operasi JDBC
java -cp target/SootUpAnalysis-1.0.0-jar-with-dependencies.jar org.example.sootup.AnalyzerWithFilter \
    src/main/java com.myapp.DataService java.sql

# Lihat penggunaan ORM (Hibernate)
java -cp target/SootUpAnalysis-1.0.0-jar-with-dependencies.jar org.example.sootup.AnalyzerWithFilter \
    src/main/java com.myapp.DataService org.hibernate
```

---

## How to Build

```bash
# Compile dan package
mvn clean package -DskipTests

# JAR akan tersedia di: target/SootUpAnalysis-1.0.0-jar-with-dependencies.jar
```

---

## Dependency Requirements

- **Java 17+** (untuk compilation)
- **Maven 3.6+**
- **SootUp 2.0.0**
- **Gson 2.10.1**
- **Guava 32.1.3**

Semua dependencies sudah terdefinisi di `pom.xml`.

---

## Tips & Tricks

### 1. Menyimpan output dengan nama custom
```bash
java -cp target/SootUpAnalysis-1.0.0-jar-with-dependencies.jar org.example.sootup.AnalyzerWithFilter \
    external_code com.example.Main java.io > analysis_java_io.json
```

### 2. Menganalisis multiple packages
Jalankan analyzer beberapa kali dengan dependency package berbeda:
```bash
java -cp target/SootUpAnalysis-1.0.0-jar-with-dependencies.jar org.example.sootup.AnalyzerWithFilter \
    src com.app.Main java.io > java_io_analysis.json

java -cp target/SootUpAnalysis-1.0.0-jar-with-dependencies.jar org.example.sootup.AnalyzerWithFilter \
    src com.app.Main java.sql > java_sql_analysis.json

java -cp target/SootUpAnalysis-1.0.0-jar-with-dependencies.jar org.example.sootup.AnalyzerWithFilter \
    src com.app.Main org.springframework > springframework_analysis.json
```

### 3. Parsing JSON output
```bash
# Extract hanya call graph
cat output/analysis_v8.json | jq '.call_graph'

# Extract methods yang terkait dependency
cat output/analysis_v8.json | jq '.methods[] | select(.package | startswith("java.io"))'

# Count calls per method
cat output/analysis_v8.json | jq '.call_graph | group_by(.src) | map({src: .[0].src, count: length})'
```

---

## Troubleshooting

### Error: "JavaCompiler tidak ditemukan"
**Solusi:** Gunakan JDK, bukan JRE. Set `JAVA_HOME` ke JDK installation.

### Error: "package X does not exist"
**Solusi:** Copy dependency JARs ke folder `libs/` dan gunakan AnalyzerWithDependencies atau AnalyzerWithFilter dengan libs path.

### Slow analysis
**Tips:** 
- Filter dengan specific dependency untuk mengurangi output
- Gunakan smaller source code folders
- Compile dengan `-cp` untuk classpath optimization

---

## Output Interpretation

### Call Graph Entry:
```json
{
  "src": "<com.example.Main: void main(java.lang.String[])>",
  "tgt": "<java.io.PrintStream: void println(java.lang.String)>",
  "line": 9,
  "src_package": "com.example",
  "tgt_package": "java.io",
  "matches": ["TARGET_MATCHES"]
}
```

**Penjelasan:**
- `src`: Method yang melakukan pemanggilan (caller)
- `tgt`: Method yang dipanggil (callee)
- `line`: Nomor baris di source code
- `src_package`: Package dari caller
- `tgt_package`: Package dari callee
- `matches`: Hasil matching dengan target dependency filter

### Method Entry:
```json
{
  "class": "com.example.Main",
  "method": "<com.example.Main: void main(java.lang.String[])>",
  "package": "com.example",
  "jimple": [...]
}
```

**Penjelasan:**
- `class`: Fully qualified class name
- `method`: Method signature
- `package`: Package name
- `jimple`: Intermediate representation (Jimple bytecode) dari method

---

## Citation & References

- **SootUp**: https://github.com/soot-oss/SootUp
- **Gson**: https://github.com/google/gson
- **Jimple IR**: https://soot-oss.github.io/soot/

