package kz.edu.daa.graph.scc;

import kz.edu.daa.graph.model.Graph;
import kz.edu.daa.graph.metrics.SimpleMetrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SCCTopologicalTest {
    private Graph graphWithCycle;
    private Graph graphDAG;
    private SimpleMetrics metrics;

    @BeforeEach
    public void setUp() {
        metrics = new SimpleMetrics();

        // Create a graph with a cycle: 0->1->2->0
        graphWithCycle = new Graph(3, true);
        graphWithCycle.addEdge(0, 1, 1);
        graphWithCycle.addEdge(1, 2, 1);
        graphWithCycle.addEdge(2, 0, 1);

        // Create a DAG: 0->1->2
        graphDAG = new Graph(3, true);
        graphDAG.addEdge(0, 1, 1);
        graphDAG.addEdge(1, 2, 1);
    }

    @Test
    public void testSingleCycleSCC() {
        TarjanSCC scc = new TarjanSCC(graphWithCycle, metrics);
        List<List<Integer>> sccs = scc.findSCCs();

        assertEquals(1, sccs.size(), "Graph with cycle should have 1 SCC");
        assertEquals(3, sccs.get(0).size(), "SCC should contain all 3 vertices");
    }

    @Test
    public void testDAGSCC() {
        TarjanSCC scc = new TarjanSCC(graphDAG, metrics);
        List<List<Integer>> sccs = scc.findSCCs();

        assertEquals(3, sccs.size(), "DAG should have 3 SCCs (one per vertex)");
        for (List<Integer> component : sccs) {
            assertEquals(1, component.size(), "Each SCC in DAG should have 1 vertex");
        }
    }

    @Test
    public void testSCCMetrics() {
        TarjanSCC scc = new TarjanSCC(graphWithCycle, metrics);
        scc.findSCCs();

        assertTrue(metrics.getOperationCount("DFS visits") > 0, "DFS visits should be counted");
        assertTrue(metrics.getOperationCount("Edges explored") > 0, "Edges explored should be counted");
        assertTrue(metrics.getTimeMs("Tarjan SCC") >= 0, "Time should be measured");
    }

    @Test
    public void testCondensationGraph() {
        TarjanSCC scc = new TarjanSCC(graphWithCycle, metrics);
        List<List<Integer>> sccs = scc.findSCCs();

        CondensationGraph condGraph = new CondensationGraph(graphWithCycle, sccs);

        assertEquals(1, condGraph.getNumSCCs(), "Condensation graph should have 1 SCC");
        assertEquals(0, condGraph.getCondensationDAG().getNumEdges(),
                "Condensation DAG from single SCC should have no edges");
    }

    @Test
    public void testComplexGraph() {
        // Create a more complex graph with multiple SCCs
        Graph complexGraph = new Graph(6, true);
        // SCC 1: 0,1,2 (with cycle)
        complexGraph.addEdge(0, 1, 1);
        complexGraph.addEdge(1, 2, 1);
        complexGraph.addEdge(2, 0, 1);

        // SCC 2: 3,4 (with cycle)
        complexGraph.addEdge(3, 4, 1);
        complexGraph.addEdge(4, 3, 1);

        // Edge from SCC1 to SCC2
        complexGraph.addEdge(2, 3, 1);

        // Isolated: 5

        TarjanSCC scc = new TarjanSCC(complexGraph, metrics);
        List<List<Integer>> sccs = scc.findSCCs();

        assertEquals(4, sccs.size(), "Complex graph should have 4 SCCs");

        CondensationGraph condGraph = new CondensationGraph(complexGraph, sccs);
        assertTrue(condGraph.getCondensationDAG().getNumEdges() > 0,
                "Condensation DAG should have edges");
    }
}
