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

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import sootup.callgraph.CallGraph;
import sootup.callgraph.CallGraph.Call;
import sootup.callgraph.CallGraphAlgorithm;
import sootup.callgraph.ClassHierarchyAnalysisAlgorithm;
import sootup.core.inputlocation.AnalysisInputLocation;
import sootup.java.bytecode.frontend.inputlocation.*;

import sootup.core.model.SootMethod;
import sootup.core.signatures.MethodSignature;
import sootup.core.types.ArrayType;
import sootup.core.types.ClassType;
import sootup.core.types.VoidType;
import sootup.java.core.JavaSootClass;
import sootup.java.core.JavaIdentifierFactory;
import sootup.java.core.views.JavaView;

public class AnalyzerWithFilter {

    public static void main(String[] args) {
        try {
            if (args.length < 3) {
                System.err.println("Usage:");
                System.err.println("java -cp <jar> org.example.sootup.AnalyzerWithFilter <source-folder> <main-class> <dependency-prefix> [libs-folder]");
                System.err.println("Example: java -jar target/SootUpAnalysis-1.0.0-jar-with-dependencies.jar external_cve com.example.Main org.apache.poi libs");
                return;
            }

            String sourceFolder = args[0];
            String mainClass = args[1];
            String depPrefix = args[2];
            String libsFolder = args.length > 3 ? args[3] : "libs";

            System.out.println("AnalyzerWithFilter — source=" + sourceFolder + " main=" + mainClass + " depPrefix=" + depPrefix + " libs=" + libsFolder);

            Path tempOut = Paths.get("temp_classes");
            deleteDirectory(tempOut.toFile());
            java.nio.file.Files.createDirectories(tempOut);

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
                System.err.println("❌ Tidak ada file .java ditemukan di " + sourceFolder);
                return;
            }

            // build classpath from libsFolder if exists
            StringBuilder classPath = new StringBuilder();
            File libDir = new File(libsFolder);
            if (libDir.exists() && libDir.isDirectory()) {
                File[] jars = libDir.listFiles((d, n) -> n.endsWith(".jar"));
                if (jars != null) {
                    for (File j : jars) {
                        if (classPath.length() > 0) classPath.append(File.pathSeparator);
                        classPath.append(j.getAbsolutePath());
                    }
                }
            }

            List<String> compileArgs = new ArrayList<>();
            compileArgs.add("-d");
            compileArgs.add(tempOut.toString());
            if (classPath.length() > 0) {
                compileArgs.add("-cp");
                compileArgs.add(classPath.toString());
            }
            compileArgs.addAll(javaFiles);

            int code = compiler.run(null, null, null, compileArgs.toArray(new String[0]));
            if (code != 0) {
                System.err.println("❌ Kompilasi gagal (cek dependency). Output di target/classes jika ada.");
                return;
            }

            // load into SootUp
            List<AnalysisInputLocation> inputLocations = new ArrayList<>();
            inputLocations.add(new JavaClassPathAnalysisInputLocation(tempOut.toString()));

            if (libDir.exists() && libDir.isDirectory()) {
                File[] jars = libDir.listFiles((d, n) -> n.endsWith(".jar"));
                if (jars != null) for (File j : jars) {
                    inputLocations.add(new JavaClassPathAnalysisInputLocation(j.getAbsolutePath()));
                }
            }

            JavaView view = new JavaView(inputLocations);
            JavaIdentifierFactory idf = JavaIdentifierFactory.getInstance();

            ClassType mainType = idf.getClassType(mainClass);
            java.util.Optional<JavaSootClass> maybeMain = view.getClass(mainType);
            if (maybeMain.isEmpty()) {
                System.err.println("❌ Main class tidak ditemukan: " + mainClass);
                return;
            }

            // build entry signature
            ClassType stringType = idf.getClassType("java.lang.String");
            ArrayType stringArrayType = idf.getArrayType(stringType, 1);

            MethodSignature entrySig = idf.getMethodSignature(
                    mainType,
                    idf.getMethodSubSignature("main", VoidType.getInstance(), Collections.singletonList(stringArrayType))
            );

            if (view.getMethod(entrySig).isEmpty()) {
                System.err.println("❌ MAIN METHOD NOT FOUND in " + mainClass);
                return;
            }

            CallGraphAlgorithm cha = new ClassHierarchyAnalysisAlgorithm(view);
            CallGraph cg = cha.initialize(Collections.singletonList(entrySig));

            Collection<Call> calls = cg.callsFrom(entrySig);

            System.out.println("Total calls in graph: " + calls.size());

            // Build reverse adjacency: tgtMethod -> set(srcMethod)
            Map<String, Set<String>> reverse = new HashMap<>();
            Map<String, Call> edgeMap = new HashMap<>();
            Set<String> allMethods = new HashSet<>();

            for (Call c : calls) {
                String src = c.getSourceMethodSignature().toString();
                String tgt = c.getTargetMethodSignature().toString();
                allMethods.add(src); allMethods.add(tgt);
                reverse.computeIfAbsent(tgt, k -> new HashSet<>()).add(src);
                edgeMap.put(src + "->" + tgt, c);
            }

            // find initial target methods whose class matches depPrefix
            Set<String> targetMethods = new HashSet<>();
            for (String m : allMethods) {
                String cls = m.split(":")[0].replaceAll("[<>]", "").trim();
                if (cls.startsWith(depPrefix)) targetMethods.add(m);
            }

            System.out.println("Target methods matching prefix (count): " + targetMethods.size());

            // reverse BFS from target methods to collect all callers that reach targets
            Set<String> reaching = new HashSet<>(targetMethods);
            Deque<String> q = new ArrayDeque<>(targetMethods);
            while (!q.isEmpty()) {
                String cur = q.poll();
                Set<String> callers = reverse.getOrDefault(cur, Collections.emptySet());
                for (String s : callers) {
                    if (reaching.add(s)) q.add(s);
                }
            }

            // collect edges that are on paths to targets (i.e., edges where tgt in reaching)
            List<Map<String, Object>> filteredEdges = new ArrayList<>();
            for (Map.Entry<String, Call> e : edgeMap.entrySet()) {
                String key = e.getKey();
                String[] parts = key.split("->", 2);
                String src = parts[0];
                String tgt = parts[1];
                if (reaching.contains(tgt)) {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("src", src);
                    m.put("tgt", tgt);
                    filteredEdges.add(m);
                }
            }

            System.out.println("Filtered edges (reaching prefix): " + filteredEdges.size());

            // export JSON
            File out = new File("output"); out.mkdirs();
            Map<String, Object> outJson = new LinkedHashMap<>();
            outJson.put("dependency_prefix", depPrefix);
            outJson.put("target_methods_count", targetMethods.size());
            outJson.put("filtered_edges", filteredEdges);

            try (FileWriter fw = new FileWriter("output/analysis_filtered.json")) {
                Gson g = new GsonBuilder().setPrettyPrinting().create();
                g.toJson(outJson, fw);
            }

            System.out.println("✔ Exported output/analysis_filtered.json");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void deleteDirectory(File f) {
        if (!f.exists()) return;
        if (f.isDirectory()) for (File c : Objects.requireNonNull(f.listFiles())) deleteDirectory(c);
        f.delete();
    }
}
