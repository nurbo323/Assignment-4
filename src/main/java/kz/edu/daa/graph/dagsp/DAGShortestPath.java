package kz.edu.daa.graph.dagsp;

import kz.edu.daa.graph.model.Edge;
import kz.edu.daa.graph.model.Graph;
import kz.edu.daa.graph.metrics.Metrics;
import kz.edu.daa.graph.topo.KahnAlgorithm;

import java.util.*;

public class DAGShortestPath {
    private final Graph graph;
    private final Metrics metrics;
    private final int source;
    private final long[] distances;
    private final int[] predecessors;

    public DAGShortestPath(Graph graph, int source, Metrics metrics) {
        this.graph = graph;
        this.source = source;
        this.metrics = metrics;
        this.distances = new long[graph.getNumVertices()];
        this.predecessors = new int[graph.getNumVertices()];

        Arrays.fill(distances, Long.MAX_VALUE);
        Arrays.fill(predecessors, -1);
        distances[source] = 0;
    }

    public void computeShortestPaths() {
        long startTime = System.nanoTime();

        // Get topological order
        KahnAlgorithm topoSort = new KahnAlgorithm(graph, metrics);
        List<Integer> topoOrder = topoSort.sort();

        // If not a valid DAG, return
        if (!topoSort.isValidDAG()) {
            System.err.println("ERROR: Graph is not a DAG!");
            return;
        }

        // Relax edges in topological order
        for (int u : topoOrder) {
            if (distances[u] != Long.MAX_VALUE) {
                for (Edge edge : graph.getEdges(u)) {
                    int v = edge.getTo();
                    long newDist = distances[u] + edge.getWeight();

                    metrics.incrementOperations("Edge relaxations");

                    if (newDist < distances[v]) {
                        distances[v] = newDist;
                        predecessors[v] = u;
                        metrics.incrementOperations("Distance updates");
                    }
                }
            }
        }

        long endTime = System.nanoTime();
        metrics.addTime("DAG Shortest Path", endTime - startTime);
    }

    public long getDistance(int vertex) {
        return distances[vertex];
    }

    public List<Integer> getPath(int vertex) {
        if (distances[vertex] == Long.MAX_VALUE) {
            return new ArrayList<>(); // No path exists
        }

        List<Integer> path = new ArrayList<>();
        int current = vertex;
        while (current != -1) {
            path.add(0, current);
            current = predecessors[current];
        }
        return path;
    }

    public void printPaths() {
        System.out.println("\n=== Shortest Paths from vertex " + source + " ===");
        for (int v = 0; v < graph.getNumVertices(); v++) {
            if (distances[v] == Long.MAX_VALUE) {
                System.out.printf("Vertex %d: UNREACHABLE\n", v);
            } else {
                System.out.printf("Vertex %d: distance=%d, path=%s\n", v, distances[v], getPath(v));
            }
        }
    }
}
