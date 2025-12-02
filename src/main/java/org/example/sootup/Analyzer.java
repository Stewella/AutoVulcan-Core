package org.example.sootup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.file.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import sootup.callgraph.CallGraph;
import sootup.callgraph.CallGraph.Call;
import sootup.callgraph.CallGraphAlgorithm;
import sootup.callgraph.ClassHierarchyAnalysisAlgorithm;
import sootup.core.inputlocation.AnalysisInputLocation;
import sootup.java.bytecode.frontend.inputlocation.*;

import sootup.core.model.SootClass;
import sootup.core.model.SootMethod;
import sootup.core.signatures.MethodSignature;
import sootup.core.typehierarchy.ViewTypeHierarchy;
import sootup.core.types.ArrayType;
import sootup.core.types.ClassType;
import sootup.core.types.VoidType;
import sootup.java.core.JavaSootClass;
import sootup.java.core.JavaIdentifierFactory;
import sootup.java.core.views.JavaView;

import guru.nidi.graphviz.engine.*;

public class Analyzer {

    public Analyzer() {
        // Default constructor
    }

    public static void main(String[] args) {
        try {

            if (args.length < 2) {
                System.err.println("Usage:");
                System.err.println("java AnalyzerV8 <source-folder> <main-class> [dependency-filter]");
                return;
            }
 
            String sourceFolder = args[0];
            String mainClass = args[1];
            String dependencyFilter = args.length > 2 ? args[2] : "";

            System.out.println("=== Analyzer V8 ===");
            System.out.println("Source folder    : " + sourceFolder);
            System.out.println("Main class       : " + mainClass);
            System.out.println("Dependency filter: " + dependencyFilter);
            System.out.println();
            
            // =========================
            // 1. COMPILE SOURCE → temp_classes/
            // =========================
            Path tempOut = Paths.get("temp_classes");
            deleteDirectory(tempOut.toFile());
            java.nio.file.Files.createDirectories(tempOut);

            System.out.println("Compiling Java source...");

            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            if (compiler == null) {
                System.err.println("❌ JavaCompiler tidak ditemukan. Gunakan JDK, bukan JRE.");
                return;
            }

            List<String> javaFiles = new ArrayList<>();
            java.nio.file.Files.walk(Paths.get(sourceFolder))
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> javaFiles.add(p.toString()));

            if (javaFiles.isEmpty()) {
                System.err.println("❌ Tidak ada file .java ditemukan.");
                return;
            }

            List<String> compileArgs = new ArrayList<>();
            compileArgs.add("-d");
            compileArgs.add(tempOut.toString());
            compileArgs.addAll(javaFiles);

            int result = compiler.run(null, null, null, compileArgs.toArray(new String[0]));
            if (result != 0) {
                System.err.println("❌ Kompilasi gagal.");
                return;
            }

            System.out.println("✔ Kompilasi sukses → Loaded into temp_classes/");
            System.out.println();
            
            // =========================
            // 2. LOAD BYTECODE ke SootUp
            // =========================
            List<AnalysisInputLocation> inputLocations = new ArrayList<>();
            inputLocations.add(new JavaClassPathAnalysisInputLocation(tempOut.toString()));

            // (Opsional) load JAR libs external
            Path libs = Paths.get("libs");
            if (libs.toFile().exists()) {
                for (File jar : libs.toFile().listFiles((d, n) -> n.endsWith(".jar"))) {
                    inputLocations.add(new JavaClassPathAnalysisInputLocation(jar.getAbsolutePath()));
                }
            }

            JavaView view = new JavaView(inputLocations);
            JavaIdentifierFactory idf = JavaIdentifierFactory.getInstance();
            
            // =========================
            // 3. LOAD MAIN CLASS
            // =========================
            ClassType mainType = idf.getClassType(mainClass);
            Optional<JavaSootClass> maybeMain = view.getClass(mainType);
            if (maybeMain.isEmpty()) {
                System.err.println("❌ Main class tidak ditemukan: " + mainClass);
                return;
            }
            JavaSootClass mainCls = maybeMain.get();
            

            // =========================
            // 4. BUILD ENTRY METHOD SIGNATURE: main(String[])
            // =========================
            ClassType stringType = idf.getClassType("java.lang.String");
            ArrayType stringArrayType = idf.getArrayType(stringType, 1); // String[]

            MethodSignature entryMethodSignature = idf.getMethodSignature(
                    mainType,
                    idf.getMethodSubSignature(
                            "main",
                            VoidType.getInstance(),
                            Collections.singletonList(stringArrayType)
                    )
            );

            if (view.getMethod(entryMethodSignature).isEmpty()) {
                System.err.println("❌ MAIN METHOD NOT FOUND in " + mainClass);
                System.err.println("   Pastikan deklarasinya: public static void main(String[] args)");
                return;
            }

            // =========================
            // 5. BANGUN CALL GRAPH (CHA)
            // =========================
            CallGraphAlgorithm cha = new ClassHierarchyAnalysisAlgorithm(view);
            CallGraph cg = cha.initialize(Collections.singletonList(entryMethodSignature));
            
            Collection<Call> calls = cg.callsFrom(entryMethodSignature);
            Set<Call> callSet = new HashSet<>(calls);
            
            System.out.println("✔ CallGraph built. Total calls: " + callSet.size());

            // ===========================
            // 6. EXPORT CALL GRAPH
            // ===========================
            List<Map<String, Object>> callEdges = new ArrayList<>();

            for (Call c : callSet) {
                Map<String, Object> e = new LinkedHashMap<>();
                e.put("src", c.getSourceMethodSignature().toString());
                e.put("tgt", c.getTargetMethodSignature().toString());
                callEdges.add(e);
            }

            System.out.println("✔ CallGraph extracted: " + callEdges.size() + " edges");

            // ===========================
            // 7. EXTRACT CFG PER METHOD
            // ===========================
            List<Map<String, Object>> methodResults = new ArrayList<>();

            Collection<JavaSootClass> allClasses = new ArrayList<>();
            view.getClasses().forEach(allClasses::add);

            for (JavaSootClass cls : allClasses) {
                for (SootMethod m : cls.getMethods()) {

                    Map<String, Object> mJson = new LinkedHashMap<>();
                    mJson.put("class", cls.getType().toString());
                    mJson.put("method", m.getSignature().toString());

                    // 7.1 Jimple
                    List<String> stmts = new ArrayList<>();
                    try {
                        m.getBody().getStmts().forEach(s -> stmts.add(s.toString()));
                    } catch (Exception e) {
                        stmts.add("<<no body>>");
                    }
                    mJson.put("jimple", stmts);

                    // 7.2 CFG
                    List<Map<String, Object>> cfgNodes = new ArrayList<>();

                    try {
                        Object body = m.getBody();
                        java.lang.reflect.Method getCfg = body.getClass().getMethod("getCfg");
                        Object cfg = getCfg.invoke(body);

                        if (cfg != null) {
                            java.lang.reflect.Method getNodes = cfg.getClass().getMethod("getNodes");
                            Object nodesObj = getNodes.invoke(cfg);

                            if (nodesObj instanceof Iterable<?> nodes) {
                                for (Object node : nodes) {
                                    Map<String, Object> nMap = new LinkedHashMap<>();
                                    nMap.put("node", node.toString());

                                    List<String> succs = new ArrayList<>();
                                    try {
                                        java.lang.reflect.Method getSucc =
                                                cfg.getClass().getMethod("getSuccessors", Object.class);
                                        Object succObj = getSucc.invoke(cfg, node);
                                        if (succObj instanceof Iterable<?> su) {
                                            for (Object s : su) succs.add(s.toString());
                                        }
                                    } catch (Exception ignore) { }

                                    nMap.put("successors", succs);
                                    cfgNodes.add(nMap);
                                }
                            }
                        }

                    } catch (NoSuchMethodException ignore) {
                        // CFG not available
                    }

                    mJson.put("cfg", cfgNodes);
                    methodResults.add(mJson);
                }
            }
            // ===========================
            // 8. EXPORT JSON
            // ===========================
            File outDir = new File("output");
            outDir.mkdirs();

            Map<String, Object> json = new LinkedHashMap<>();
            json.put("call_graph", callEdges);
            json.put("methods", methodResults);
            try (FileWriter fw = new FileWriter("output/analysis_v8.json")) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(json, fw);
            }

            System.out.println("✔ JSON exported to output/analysis_v8.json");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void deleteDirectory(File f) {
        if (!f.exists()) return;
        if (f.isDirectory()) {
            for (File c : Objects.requireNonNull(f.listFiles())) deleteDirectory(c);
        }
        f.delete();
    }
}
