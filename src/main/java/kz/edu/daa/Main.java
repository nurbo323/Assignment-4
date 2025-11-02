package kz.edu.daa;

import kz.edu.daa.graph.dagsp.DAGLongestPath;
import kz.edu.daa.graph.dagsp.DAGShortestPath;
import kz.edu.daa.graph.model.Graph;
import kz.edu.daa.graph.metrics.SimpleMetrics;
import kz.edu.daa.graph.scc.CondensationGraph;
import kz.edu.daa.graph.scc.TarjanSCC;
import kz.edu.daa.graph.topo.KahnAlgorithm;
import kz.edu.daa.utils.GraphLoader;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String DATA_DIR = "data";
    private static final String DATASETS_DIR = DATA_DIR + "/generated";
    private static final String ORIGINAL_DATA = DATA_DIR + "/tasks.json";

    public static void main(String[] args) throws IOException {
        System.out.println("========================================");
        System.out.println("DAA Assignment 4: Graph Algorithms");
        System.out.println("SCC, Topological Sort, DAG Shortest Paths");
        System.out.println("========================================\n");

        // Run on original dataset
        System.out.println("\n>>> Running on original dataset (tasks.json)");
        runOnDataset(ORIGINAL_DATA);

        // Run on all generated datasets
        System.out.println("\n\n>>> Running on generated datasets");
        runOnAllGeneratedDatasets();

        System.out.println("\n========================================");
        System.out.println("All tests completed!");
        System.out.println("========================================");
    }

    private static void runOnDataset(String filePath) throws IOException {
        System.out.println("\n--- Processing: " + filePath + " ---");

        try {
            Graph graph = GraphLoader.loadFromJson(filePath);
            SimpleMetrics metrics = new SimpleMetrics();

            System.out.printf("Graph loaded: %d vertices, %d edges\n",
                    graph.getNumVertices(), graph.getNumEdges());

            // Step 1: Find SCCs using Tarjan
            System.out.println("\n[Step 1] Finding Strongly Connected Components (Tarjan)...");
            TarjanSCC tarjanSCC = new TarjanSCC(graph, metrics);
            List<List<Integer>> sccs = tarjanSCC.findSCCs();

            System.out.printf("Found %d SCC(s)\n", sccs.size());
            for (int i = 0; i < sccs.size(); i++) {
                System.out.printf("  SCC %d: %s (size=%d)\n", i, sccs.get(i), sccs.get(i).size());
            }

            // Step 2: Build condensation graph
            System.out.println("\n[Step 2] Building Condensation Graph...");
            CondensationGraph condensationGraph = new CondensationGraph(graph, sccs);
            Graph condensationDAG = condensationGraph.getCondensationDAG();

            System.out.printf("Condensation DAG: %d vertices (SCCs), %d edges\n",
                    condensationDAG.getNumVertices(), condensationDAG.getNumEdges());

            // Step 3: Topological sort on condensation DAG
            System.out.println("\n[Step 3] Topological Sort on Condensation DAG...");
            KahnAlgorithm topoSort = new KahnAlgorithm(condensationDAG, metrics);
            List<Integer> topoOrder = topoSort.sort();

            System.out.println("Topological order of SCCs: " + topoOrder);
            System.out.println("Is valid DAG: " + topoSort.isValidDAG());

            // Step 4: Shortest paths in DAG
            if (topoSort.isValidDAG()) {
                System.out.println("\n[Step 4] Computing Shortest Paths in Condensation DAG...");
                int source = GraphLoader.getSource(filePath);
                int sourceSCC = condensationGraph.getSCCOfVertex(source);

                DAGShortestPath shortestPath = new DAGShortestPath(condensationDAG, sourceSCC, metrics);
                shortestPath.computeShortestPaths();
                shortestPath.printPaths();

                // Step 5: Longest paths (critical path)
                System.out.println("\n[Step 5] Computing Longest Paths (Critical Path) in DAG...");
                DAGLongestPath longestPath = new DAGLongestPath(condensationDAG, sourceSCC, metrics);
                longestPath.computeLongestPaths();
                longestPath.printPaths();
            } else {
                System.out.println("\n[Step 4-5] Skipped (graph is not a DAG)");
            }

            // Print metrics
            System.out.println("\n[Metrics]");
            metrics.printSummary();

        } catch (IOException e) {
            System.err.println("ERROR: Failed to load or process " + filePath);
            e.printStackTrace();
        }
    }

    private static void runOnAllGeneratedDatasets() throws IOException {
        Path datasetsPath = Paths.get(DATASETS_DIR);

        if (!Files.exists(datasetsPath)) {
            System.out.println("Generated datasets directory not found: " + DATASETS_DIR);
            return;
        }

        List<String> datasetFiles = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(datasetsPath, "*.json")) {
            for (Path path : stream) {
                datasetFiles.add(path.toString());
            }
        }

        datasetFiles.sort(String::compareTo);

        for (String dataset : datasetFiles) {
            try {
                runOnDataset(dataset);
                System.out.println("\n" + "=".repeat(50));
            } catch (Exception e) {
                System.err.println("ERROR processing " + dataset + ": " + e.getMessage());
            }
        }
    }
}
