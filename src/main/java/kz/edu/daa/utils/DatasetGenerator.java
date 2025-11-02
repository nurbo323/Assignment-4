package kz.edu.daa.utils;

import com.google.gson.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class DatasetGenerator {

    public static void generateAllDatasets(String outputDir) throws IOException {
        // Create directory if not exists
        Files.createDirectories(Paths.get(outputDir));

        // Small datasets (6-10 vertices)
        generateSmallWithCycles(outputDir);
        generateSmallDAG(outputDir);
        generateSmallMixed(outputDir);

        // Medium datasets (10-20 vertices)
        generateMediumSparse(outputDir);
        generateMediumDense(outputDir);
        generateMediumMultipleSCC(outputDir);

        // Large datasets (20-50 vertices)
        generateLargeSparse(outputDir);
        generateLargeDense(outputDir);
        generateLargeComplex(outputDir);

        System.out.println("All 9 datasets generated successfully in " + outputDir);
    }

    // Small datasets
    private static void generateSmallWithCycles(String outputDir) throws IOException {
        JsonObject dataset = new JsonObject();
        dataset.addProperty("directed", true);
        dataset.addProperty("n", 8);
        dataset.addProperty("source", 0);
        dataset.addProperty("weight_model", "edge");

        JsonArray edges = new JsonArray();
        edges.add(createEdge(0, 1, 3));
        edges.add(createEdge(1, 2, 2));
        edges.add(createEdge(2, 3, 4));
        edges.add(createEdge(3, 1, 1)); // Cycle
        edges.add(createEdge(4, 5, 2));
        edges.add(createEdge(5, 6, 5));
        edges.add(createEdge(6, 7, 1));
        edges.add(createEdge(2, 4, 3)); // Connect components

        dataset.add("edges", edges);
        writeDataset(dataset, outputDir + "/dataset_small_cycles.json");
    }

    private static void generateSmallDAG(String outputDir) throws IOException {
        JsonObject dataset = new JsonObject();
        dataset.addProperty("directed", true);
        dataset.addProperty("n", 6);
        dataset.addProperty("source", 0);
        dataset.addProperty("weight_model", "edge");

        JsonArray edges = new JsonArray();
        edges.add(createEdge(0, 1, 2));
        edges.add(createEdge(0, 2, 5));
        edges.add(createEdge(1, 3, 1));
        edges.add(createEdge(2, 3, 2));
        edges.add(createEdge(3, 4, 3));
        edges.add(createEdge(4, 5, 1));

        dataset.add("edges", edges);
        writeDataset(dataset, outputDir + "/dataset_small_dag.json");
    }

    private static void generateSmallMixed(String outputDir) throws IOException {
        JsonObject dataset = new JsonObject();
        dataset.addProperty("directed", true);
        dataset.addProperty("n", 7);
        dataset.addProperty("source", 0);
        dataset.addProperty("weight_model", "edge");

        JsonArray edges = new JsonArray();
        edges.add(createEdge(0, 1, 1));
        edges.add(createEdge(1, 2, 2));
        edges.add(createEdge(2, 1, 1)); // Cycle: 1-2-1
        edges.add(createEdge(2, 3, 3));
        edges.add(createEdge(3, 4, 2));
        edges.add(createEdge(4, 5, 1));
        edges.add(createEdge(5, 6, 2));
        edges.add(createEdge(6, 3, 1)); // Cycle: 3-4-5-6-3

        dataset.add("edges", edges);
        writeDataset(dataset, outputDir + "/dataset_small_mixed.json");
    }

    // Medium datasets
    private static void generateMediumSparse(String outputDir) throws IOException {
        JsonObject dataset = new JsonObject();
        dataset.addProperty("directed", true);
        dataset.addProperty("n", 15);
        dataset.addProperty("source", 0);
        dataset.addProperty("weight_model", "edge");

        JsonArray edges = new JsonArray();
        Random rand = new Random(42); // Fixed seed for reproducibility

        for (int i = 0; i < 15; i++) {
            for (int j = i + 1; j < 15; j++) {
                if (rand.nextDouble() < 0.15) { // 15% edge probability
                    edges.add(createEdge(i, j, rand.nextInt(10) + 1));
                }
            }
        }

        dataset.add("edges", edges);
        writeDataset(dataset, outputDir + "/dataset_medium_sparse.json");
    }

    private static void generateMediumDense(String outputDir) throws IOException {
        JsonObject dataset = new JsonObject();
        dataset.addProperty("directed", true);
        dataset.addProperty("n", 12);
        dataset.addProperty("source", 0);
        dataset.addProperty("weight_model", "edge");

        JsonArray edges = new JsonArray();
        Random rand = new Random(43);

        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                if (i != j && rand.nextDouble() < 0.4) { // 40% edge probability
                    edges.add(createEdge(i, j, rand.nextInt(10) + 1));
                }
            }
        }

        dataset.add("edges", edges);
        writeDataset(dataset, outputDir + "/dataset_medium_dense.json");
    }

    private static void generateMediumMultipleSCC(String outputDir) throws IOException {
        JsonObject dataset = new JsonObject();
        dataset.addProperty("directed", true);
        dataset.addProperty("n", 18);
        dataset.addProperty("source", 0);
        dataset.addProperty("weight_model", "edge");

        JsonArray edges = new JsonArray();

        // SCC 1: 0-1-2-0
        edges.add(createEdge(0, 1, 2));
        edges.add(createEdge(1, 2, 3));
        edges.add(createEdge(2, 0, 1));

        // SCC 2: 3-4-5-3
        edges.add(createEdge(3, 4, 1));
        edges.add(createEdge(4, 5, 2));
        edges.add(createEdge(5, 3, 1));

        // SCC 3: 6-7-8-6
        edges.add(createEdge(6, 7, 3));
        edges.add(createEdge(7, 8, 2));
        edges.add(createEdge(8, 6, 1));

        // Connections between SCCs
        edges.add(createEdge(2, 3, 4));
        edges.add(createEdge(5, 6, 3));
        edges.add(createEdge(8, 9, 2));

        // Remaining nodes (10-17) as a chain
        for (int i = 9; i < 17; i++) {
            edges.add(createEdge(i, i + 1, 1));
        }

        dataset.add("edges", edges);
        writeDataset(dataset, outputDir + "/dataset_medium_multiple_scc.json");
    }

    // Large datasets
    private static void generateLargeSparse(String outputDir) throws IOException {
        JsonObject dataset = new JsonObject();
        dataset.addProperty("directed", true);
        dataset.addProperty("n", 30);
        dataset.addProperty("source", 0);
        dataset.addProperty("weight_model", "edge");

        JsonArray edges = new JsonArray();
        Random rand = new Random(44);

        for (int i = 0; i < 30; i++) {
            for (int j = i + 1; j < 30; j++) {
                if (rand.nextDouble() < 0.1) { // 10% edge probability
                    edges.add(createEdge(i, j, rand.nextInt(20) + 1));
                }
            }
        }

        dataset.add("edges", edges);
        writeDataset(dataset, outputDir + "/dataset_large_sparse.json");
    }

    private static void generateLargeDense(String outputDir) throws IOException {
        JsonObject dataset = new JsonObject();
        dataset.addProperty("directed", true);
        dataset.addProperty("n", 25);
        dataset.addProperty("source", 0);
        dataset.addProperty("weight_model", "edge");

        JsonArray edges = new JsonArray();
        Random rand = new Random(45);

        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 25; j++) {
                if (i != j && rand.nextDouble() < 0.35) { // 35% edge probability
                    edges.add(createEdge(i, j, rand.nextInt(20) + 1));
                }
            }
        }

        dataset.add("edges", edges);
        writeDataset(dataset, outputDir + "/dataset_large_dense.json");
    }

    private static void generateLargeComplex(String outputDir) throws IOException {
        JsonObject dataset = new JsonObject();
        dataset.addProperty("directed", true);
        dataset.addProperty("n", 40);
        dataset.addProperty("source", 0);
        dataset.addProperty("weight_model", "edge");

        JsonArray edges = new JsonArray();
        Random rand = new Random(46);

        // Create multiple SCCs and DAG structure
        for (int scc = 0; scc < 4; scc++) {
            int base = scc * 10;
            // Create SCC as a cycle within each group
            for (int i = base; i < base + 9; i++) {
                edges.add(createEdge(i, i + 1, rand.nextInt(15) + 1));
            }
            edges.add(createEdge(base + 9, base, rand.nextInt(15) + 1)); // Close cycle
        }

        // Connect SCCs
        edges.add(createEdge(9, 10, rand.nextInt(15) + 1));
        edges.add(createEdge(19, 20, rand.nextInt(15) + 1));
        edges.add(createEdge(29, 30, rand.nextInt(15) + 1));

        // Random edges within each group
        for (int scc = 0; scc < 4; scc++) {
            int base = scc * 10;
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (i != j && rand.nextDouble() < 0.15) {
                        edges.add(createEdge(base + i, base + j, rand.nextInt(15) + 1));
                    }
                }
            }
        }

        dataset.add("edges", edges);
        writeDataset(dataset, outputDir + "/dataset_large_complex.json");
    }

    private static JsonObject createEdge(int u, int v, int w) {
        JsonObject edge = new JsonObject();
        edge.addProperty("u", u);
        edge.addProperty("v", v);
        edge.addProperty("w", w);
        return edge;
    }

    private static void writeDataset(JsonObject dataset, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(dataset, writer);
        }
        System.out.println("Generated: " + filePath);
    }

    public static void main(String[] args) throws IOException {
        String outputDir = "data/generated";
        generateAllDatasets(outputDir);
    }
}
