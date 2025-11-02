package kz.edu.daa.graph.topo;

import kz.edu.daa.graph.model.Edge;
import kz.edu.daa.graph.model.Graph;
import kz.edu.daa.graph.metrics.Metrics;

import java.util.*;

public class KahnAlgorithm {
    private final Graph graph;
    private final Metrics metrics;
    private List<Integer> topologicalOrder;

    public KahnAlgorithm(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
        this.topologicalOrder = new ArrayList<>();
    }

    public List<Integer> sort() {
        long startTime = System.nanoTime();
        topologicalOrder.clear();

        // Calculate in-degrees for all vertices
        int[] inDegree = new int[graph.getNumVertices()];
        for (int u = 0; u < graph.getNumVertices(); u++) {
            for (Edge edge : graph.getEdges(u)) {
                inDegree[edge.getTo()]++;
                metrics.incrementOperations("In-degree calculations");
            }
        }

        // Queue of vertices with in-degree 0
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < graph.getNumVertices(); i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
                metrics.incrementOperations("Queue insertions");
            }
        }

        // Process vertices with in-degree 0
        while (!queue.isEmpty()) {
            int u = queue.poll();
            metrics.incrementOperations("Queue removals");
            topologicalOrder.add(u);

            for (Edge edge : graph.getEdges(u)) {
                int v = edge.getTo();
                inDegree[v]--;
                metrics.incrementOperations("Edge relaxations");

                if (inDegree[v] == 0) {
                    queue.offer(v);
                    metrics.incrementOperations("Queue insertions");
                }
            }
        }

        // Check for cycles
        if (topologicalOrder.size() != graph.getNumVertices()) {
            System.err.println("WARNING: Graph contains a cycle!");
        }

        long endTime = System.nanoTime();
        metrics.addTime("Kahn Topological Sort", endTime - startTime);

        return topologicalOrder;
    }

    public List<Integer> getTopologicalOrder() {
        return new ArrayList<>(topologicalOrder);
    }

    public boolean isValidDAG() {
        return topologicalOrder.size() == graph.getNumVertices();
    }
}
