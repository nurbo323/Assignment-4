package kz.edu.daa.graph.scc;

import kz.edu.daa.graph.model.Edge;
import kz.edu.daa.graph.model.Graph;
import kz.edu.daa.graph.metrics.Metrics;

import java.util.*;

public class TarjanSCC {
    private final Graph graph;
    private final Metrics metrics;
    private final List<List<Integer>> sccs;
    private final Stack<Integer> stack;
    private final int[] ids;
    private final int[] lowlinks;
    private final boolean[] onStack;
    private int id;

    public TarjanSCC(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
        this.sccs = new ArrayList<>();
        this.stack = new Stack<>();
        this.ids = new int[graph.getNumVertices()];
        this.lowlinks = new int[graph.getNumVertices()];
        this.onStack = new boolean[graph.getNumVertices()];
        this.id = 0;

        Arrays.fill(ids, -1);
    }

    public List<List<Integer>> findSCCs() {
        long startTime = System.nanoTime();

        for (int i = 0; i < graph.getNumVertices(); i++) {
            if (ids[i] == -1) {
                dfs(i);
            }
        }

        long endTime = System.nanoTime();
        metrics.addTime("Tarjan SCC", endTime - startTime);

        return sccs;
    }

    private void dfs(int vertex) {
        metrics.incrementOperations("DFS visits");

        ids[vertex] = lowlinks[vertex] = id++;
        stack.push(vertex);
        onStack[vertex] = true;

        List<Edge> edges = graph.getEdges(vertex);
        for (Edge edge : edges) {
            metrics.incrementOperations("Edges explored");
            int to = edge.getTo();

            if (ids[to] == -1) {
                dfs(to);
                lowlinks[vertex] = Math.min(lowlinks[vertex], lowlinks[to]);
            } else if (onStack[to]) {
                lowlinks[vertex] = Math.min(lowlinks[vertex], ids[to]);
            }
        }

        if (ids[vertex] == lowlinks[vertex]) {
            List<Integer> scc = new ArrayList<>();
            while (true) {
                int v = stack.pop();
                onStack[v] = false;
                scc.add(v);
                if (v == vertex) break;
            }
            sccs.add(scc);
            metrics.incrementOperations("SCCs found");
        }
    }

    public List<List<Integer>> getSCCs() {
        return sccs;
    }

    public List<Integer> getSCCSizes() {
        List<Integer> sizes = new ArrayList<>();
        for (List<Integer> scc : sccs) {
            sizes.add(scc.size());
        }
        return sizes;
    }
}
