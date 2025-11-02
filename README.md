# Assignment 4: Design and Analysis of Algorithms
## Smart City/Campus Scheduling - Graph Algorithms

### Overview
This project consolidates two major graph algorithm topics:
1. **Strongly Connected Components (SCC)** using Tarjan's Algorithm
2. **Topological Ordering** using Kahn's Algorithm
3. **Shortest Paths in DAGs** using Dynamic Programming

The application processes dependency graphs, identifies cycles (compresses them into SCCs),
and computes optimal execution paths for scheduling tasks.

---

## Features

### Algorithms Implemented

#### 1. Tarjan's SCC Algorithm
- Finds all strongly connected components in O(V + E) time
- Uses a stack-based DFS approach with low-link values
- Outputs list of components and condensation graph

**Time Complexity:** O(V + E)
**Space Complexity:** O(V)

#### 2. Kahn's Topological Sort (BFS-based)
- Produces valid topological ordering of a DAG
- Detects cycles in directed graphs
- Works on condensation graph (DAG of SCCs)

**Time Complexity:** O(V + E)
**Space Complexity:** O(V)

#### 3. DAG Shortest Paths
- Single-source shortest path computation
- Uses topological order + edge relaxation
- Supports negative weights (in DAGs only)

**Time Complexity:** O(V + E)
**Space Complexity:** O(V)

#### 4. DAG Longest Paths (Critical Path)
- Computes critical path for project scheduling
- Maximizes path weight instead of minimizing
- Essential for deadline analysis

**Time Complexity:** O(V + E)
**Space Complexity:** O(V)

---

## Project Structure

assignment4-daa/
├── src/
│ ├── main/java/kz/edu/daa/
│ │ ├── graph/
│ │ │ ├── model/
│ │ │ │ ├── Graph.java # Directed graph using adjacency list
│ │ │ │ └── Edge.java # Edge representation with weight
│ │ │ ├── metrics/
│ │ │ │ ├── Metrics.java # Metrics interface
│ │ │ │ └── SimpleMetrics.java # Implementation
│ │ │ ├── scc/
│ │ │ │ ├── TarjanSCC.java # Tarjan's SCC algorithm
│ │ │ │ └── CondensationGraph.java # Condensation graph builder
│ │ │ ├── topo/
│ │ │ │ └── KahnAlgorithm.java # Kahn's topological sort
│ │ │ └── dagsp/
│ │ │ ├── DAGShortestPath.java # Shortest paths in DAG
│ │ │ └── DAGLongestPath.java # Longest paths (critical path)
│ │ ├── utils/
│ │ │ ├── GraphLoader.java # JSON graph loader
│ │ │ └── DatasetGenerator.java # Generates 9 test datasets
│ │ └── Main.java # Main entry point
│ └── test/java/kz/edu/daa/graph/
│ ├── scc/SCCTopologicalTest.java
│ └── dagsp/DAGShortestPathTest.java
├── data/
│ ├── tasks.json # Original test dataset
│ └── generated/ # 9 auto-generated datasets
│ ├── dataset_small_cycles.json
│ ├── dataset_small_dag.json
│ ├── dataset_small_mixed.json
│ ├── dataset_medium_sparse.json
│ ├── dataset_medium_dense.json
│ ├── dataset_medium_multiple_scc.json
│ ├── dataset_large_sparse.json
│ ├── dataset_large_dense.json
│ └── dataset_large_complex.json
├── pom.xml
├── README.md
└── .gitignore

text

---

## Building and Running

### Prerequisites
- Java 17+
- Maven 3.8+

### Build the Project

mvn clean compile

text

### Run All Tests

mvn test

text

### Run the Main Application

mvn exec:java -Dexec.mainClass="kz.edu.daa.Main"

text

Or directly from IntelliJ: Right-click `Main.java` → Run

### Generate Datasets

mvn exec:java -Dexec.mainClass="kz.edu.daa.utils.DatasetGenerator"

text

---

## Usage Examples

### Loading a Graph from JSON

Graph graph = GraphLoader.loadFromJson("data/tasks.json");

text

### Finding Strongly Connected Components

SimpleMetrics metrics = new SimpleMetrics();
TarjanSCC scc = new TarjanSCC(graph, metrics);
List<List<Integer>> components = scc.findSCCs();

text

### Computing Shortest Paths

DAGShortestPath sp = new DAGShortestPath(graph, sourceVertex, metrics);
sp.computeShortestPaths();
long distance = sp.getDistance(targetVertex);
List<Integer> path = sp.getPath(targetVertex);

text

### Computing Critical Path

DAGLongestPath lp = new DAGLongestPath(graph, sourceVertex, metrics);
lp.computeLongestPaths();
long criticalPath = lp.getCriticalPathLength();

text

---

## Test Datasets

### Dataset Categories

| Category | Vertices | Edges | Characteristics |
|----------|----------|-------|-----------------|
| **Small Cycles** | 8 | 8 | Contains multiple cycles |
| **Small DAG** | 6 | 6 | Pure DAG (no cycles) |
| **Small Mixed** | 7 | 8 | Mix of cycles and chains |
| **Medium Sparse** | 15 | 8 | Low edge density (~5%) |
| **Medium Dense** | 12 | 47 | High edge density (~33%) |
| **Medium Multiple SCCs** | 18 | 20 | 4 SCCs with chain structure |
| **Large Sparse** | 30 | 54 | Sparse with many SCCs |
| **Large Dense** | 25 | 208 | Very dense (highly connected) |
| **Large Complex** | 40 | 97 | 4 large SCCs with DAG structure |

---

## Algorithm Complexity Analysis

### Tarjan's SCC
- **Time:** O(V + E) - single DFS pass
- **Space:** O(V) - for stack and id/lowlink arrays
- **Advantages:** Linear time, single pass
- **Disadvantages:** Requires understanding of lowlink values

### Kahn's Topological Sort
- **Time:** O(V + E) - compute in-degrees + BFS
- **Space:** O(V) - for queue and in-degree array
- **Advantages:** Detects cycles, intuitive
- **Disadvantages:** Cannot handle negative cycles

### DAG Shortest Path
- **Time:** O(V + E) - topological sort + relaxation
- **Space:** O(V) - for distance/predecessor arrays
- **Advantages:** Handles negative weights in DAGs
- **Disadvantages:** Only works on DAGs

### DAG Longest Path
- **Time:** O(V + E) - same as shortest path
- **Space:** O(V)
- **Advantages:** Optimal for critical path analysis
- **Disadvantages:** Only works on DAGs

---

## Performance Metrics

The application collects metrics for each algorithm:
- **DFS visits:** Number of vertex visits
- **Edges explored:** Number of edge traversals
- **Operations count:** Queue operations, distance updates
- **Execution time:** In milliseconds (ms)

Example output:
=== Metrics Summary ===
Operations:
SCCs found: 6
DFS visits: 8
Edges explored: 7
Time measurements:
Tarjan SCC: 0.0936 ms
Kahn Topological Sort: 0.5541 ms
DAG Shortest Path: 0.0745 ms

text

---

## Input Format (JSON)

Datasets use the following JSON format:

{
"directed": true,
"n": 8,
"edges": [
{"u": 0, "v": 1, "w": 3},
{"u": 1, "v": 2, "w": 2}
],
"source": 0,
"weight_model": "edge"
}

text

**Fields:**
- `directed` (boolean): Whether graph is directed
- `n` (int): Number of vertices (0 to n-1)
- `edges` (array): List of edges with source, target, weight
- `source` (int): Starting vertex for path computations
- `weight_model` (string): "edge" (weights on edges)

---

## Example Execution

========================================
DAA Assignment 4: Graph Algorithms
SCC, Topological Sort, DAG Shortest Paths
Running on original dataset (tasks.json)

--- Processing: data/tasks.json ---
Graph loaded: 8 vertices, 7 edges

[Step 1] Finding Strongly Connected Components (Tarjan)...
Found 6 SCC(s)
SCC 0: (size=3)​
SCC 1: (size=1)
...

[Step 2] Building Condensation Graph...
Condensation DAG: 6 vertices (SCCs), 4 edges

[Step 3] Topological Sort on Condensation DAG...
Topological order of SCCs:​
Is valid DAG: true

[Step 4] Computing Shortest Paths...
[Step 5] Computing Longest Paths (Critical Path)...

[Metrics]
=== Metrics Summary ===
Operations:
SCCs found: 6
Edges explored: 7
DFS visits: 8
...

text

---

## Troubleshooting

### Issue: "Graph contains a cycle"
- **Cause:** Topological sort detects cycles in the graph
- **Solution:** Ensure you're working with a DAG, or use SCC first to compress cycles

### Issue: UNREACHABLE vertices
- **Cause:** Source vertex cannot reach target vertex
- **Solution:** Check graph connectivity or select different source

### Issue: Wrong path lengths
- **Cause:** Negative weights in graph
- **Solution:** DAG algorithms support negative weights, but ensure no negative cycles

---

## References

- **Tarjan's Algorithm:** Tarjan, R. (1972). "Depth-first search and linear graph algorithms"
- **Kahn's Algorithm:** Kahn, A. (1962). "Topological Sorting of Large Networks"
- **DAG Shortest Paths:** Bellman-Ford variant for DAGs

---

## Author
DAA Assignment 4 Implementation
Date: November 2025

---

## License
Educational Project