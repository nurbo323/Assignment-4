package kz.edu.daa.graph.model;

import java.util.*;

public class Graph {
    private final int numVertices;
    private final Map<Integer, List<Edge>> adjacencyList;
    private final boolean isDirected;

    public Graph(int numVertices, boolean isDirected) {
        this.numVertices = numVertices;
        this.isDirected = isDirected;
        this.adjacencyList = new HashMap<>();

        for (int i = 0; i < numVertices; i++) {
            adjacencyList.put(i, new ArrayList<>());
        }
    }

    public void addEdge(int from, int to, int weight) {
        if (from < 0 || from >= numVertices || to < 0 || to >= numVertices) {
            throw new IllegalArgumentException("Invalid vertex: " + from + " or " + to);
        }

        Edge edge = new Edge(from, to, weight);
        adjacencyList.get(from).add(edge);

        if (!isDirected) {
            Edge reverseEdge = new Edge(to, from, weight);
            adjacencyList.get(to).add(reverseEdge);
        }
    }

    public List<Edge> getEdges(int vertex) {
        if (vertex < 0 || vertex >= numVertices) {
            throw new IllegalArgumentException("Invalid vertex: " + vertex);
        }
        return new ArrayList<>(adjacencyList.get(vertex));
    }

    public int getNumVertices() {
        return numVertices;
    }

    public int getNumEdges() {
        int count = 0;
        for (List<Edge> edges : adjacencyList.values()) {
            count += edges.size();
        }
        return isDirected ? count : count / 2;
    }

    public boolean isDirected() {
        return isDirected;
    }

    public List<Edge> getAllEdges() {
        List<Edge> allEdges = new ArrayList<>();
        for (List<Edge> edges : adjacencyList.values()) {
            allEdges.addAll(edges);
        }
        return allEdges;
    }

    public List<Integer> getVertices() {
        return new ArrayList<>(adjacencyList.keySet());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph{vertices=").append(numVertices)
                .append(", directed=").append(isDirected).append("}\n");
        for (int v : getVertices()) {
            sb.append(v).append(" -> ");
            List<Edge> edges = getEdges(v);
            for (Edge e : edges) {
                sb.append(e.getTo()).append("(w=").append(e.getWeight()).append(") ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
