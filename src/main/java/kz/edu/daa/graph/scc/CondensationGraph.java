package kz.edu.daa.graph.scc;

import kz.edu.daa.graph.model.Edge;
import kz.edu.daa.graph.model.Graph;
import java.util.*;

public class CondensationGraph {
    private final Graph originalGraph;
    private final List<List<Integer>> sccs;
    private final Graph condensationDAG;
    private final Map<Integer, Integer> vertexToSCC; // maps original vertex to SCC index

    public CondensationGraph(Graph originalGraph, List<List<Integer>> sccs) {
        this.originalGraph = originalGraph;
        this.sccs = sccs;
        this.vertexToSCC = new HashMap<>();
        this.condensationDAG = new Graph(sccs.size(), true);

        // Build vertex to SCC mapping
        for (int i = 0; i < sccs.size(); i++) {
            for (int vertex : sccs.get(i)) {
                vertexToSCC.put(vertex, i);
            }
        }

        buildCondensationGraph();
    }

    private void buildCondensationGraph() {
        Set<String> addedEdges = new HashSet<>(); // To avoid duplicate edges between SCCs

        for (int u = 0; u < originalGraph.getNumVertices(); u++) {
            int sccU = vertexToSCC.get(u);

            for (Edge edge : originalGraph.getEdges(u)) {
                int v = edge.getTo();
                int sccV = vertexToSCC.get(v);

                // If edge connects different SCCs
                if (sccU != sccV) {
                    String edgeKey = sccU + "->" + sccV;
                    if (!addedEdges.contains(edgeKey)) {
                        condensationDAG.addEdge(sccU, sccV, edge.getWeight());
                        addedEdges.add(edgeKey);
                    }
                }
            }
        }
    }

    public Graph getCondensationDAG() {
        return condensationDAG;
    }

    public int getSCCOfVertex(int vertex) {
        return vertexToSCC.get(vertex);
    }

    public List<Integer> getVerticesInSCC(int sccIndex) {
        return new ArrayList<>(sccs.get(sccIndex));
    }

    public int getNumSCCs() {
        return sccs.size();
    }

    public void printSummary() {
        System.out.println("\n=== Condensation Graph Summary ===");
        System.out.println("Number of SCCs: " + sccs.size());
        for (int i = 0; i < sccs.size(); i++) {
            System.out.printf("SCC %d: %s\n", i, sccs.get(i));
        }
        System.out.println("\nCondensation DAG:");
        System.out.println(condensationDAG);
    }
}
