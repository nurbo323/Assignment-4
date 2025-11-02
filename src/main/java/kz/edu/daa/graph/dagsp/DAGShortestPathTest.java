package kz.edu.daa.graph.dagsp;

import kz.edu.daa.graph.model.Graph;
import kz.edu.daa.graph.metrics.SimpleMetrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DAGShortestPathTest {
    private Graph simpleDAG;
    private Graph complexDAG;
    private SimpleMetrics metrics;

    @BeforeEach
    public void setUp() {
        metrics = new SimpleMetrics();

        // Simple DAG: 0->1(1) 0->2(4) 1->2(2) 1->3(1) 2->3(1)
        simpleDAG = new Graph(4, true);
        simpleDAG.addEdge(0, 1, 1);
        simpleDAG.addEdge(0, 2, 4);
        simpleDAG.addEdge(1, 2, 2);
        simpleDAG.addEdge(1, 3, 1);
        simpleDAG.addEdge(2, 3, 1);

        // Complex DAG with more vertices
        complexDAG = new Graph(6, true);
        complexDAG.addEdge(0, 1, 1);
        complexDAG.addEdge(0, 2, 5);
        complexDAG.addEdge(1, 3, 3);
        complexDAG.addEdge(2, 3, 1);
        complexDAG.addEdge(3, 4, 2);
        complexDAG.addEdge(4, 5, 1);
    }

    @Test
    public void testSimpleShortestPath() {
        DAGShortestPath sp = new DAGShortestPath(simpleDAG, 0, metrics);
        sp.computeShortestPaths();

        assertEquals(0, sp.getDistance(0), "Distance to source should be 0");
        assertEquals(1, sp.getDistance(1), "Distance to vertex 1 should be 1");
        assertEquals(3, sp.getDistance(2), "Distance to vertex 2 should be 3 (via 1)");
        assertEquals(2, sp.getDistance(3), "Distance to vertex 3 should be 2 (via 1)");
    }

    @Test
    public void testSimpleLongestPath() {
        DAGLongestPath lp = new DAGLongestPath(simpleDAG, 0, metrics);
        lp.computeLongestPaths();

        assertEquals(0, lp.getDistance(0), "Distance to source should be 0");
        assertEquals(1, lp.getDistance(1), "Distance to vertex 1 should be 1");
        assertEquals(4, lp.getDistance(2), "Distance to vertex 2 should be 4 (direct edge)");
        assertEquals(5, lp.getDistance(3), "Distance to vertex 3 should be 5 (via 0->2->3)");
    }

    @Test
    public void testPathReconstruction() {
        DAGShortestPath sp = new DAGShortestPath(simpleDAG, 0, metrics);
        sp.computeShortestPaths();

        List<Integer> path = sp.getPath(3);
        assertNotNull(path, "Path should exist");
        assertEquals(3, path.size(), "Path should have 3 vertices");
        assertTrue(path.contains(0) && path.contains(1) && path.contains(3), "Path should contain 0->1->3");
    }

    @Test
    public void testComplexDAG() {
        DAGShortestPath sp = new DAGShortestPath(complexDAG, 0, metrics);
        sp.computeShortestPaths();

        assertEquals(0, sp.getDistance(0));
        assertEquals(1, sp.getDistance(1));
        assertEquals(5, sp.getDistance(2));
        assertEquals(4, sp.getDistance(3));
        assertEquals(6, sp.getDistance(4));
        assertEquals(7, sp.getDistance(5));
    }

    @Test
    public void testCriticalPathLength() {
        DAGLongestPath lp = new DAGLongestPath(complexDAG, 0, metrics);
        lp.computeLongestPaths();

        long criticalPath = lp.getCriticalPathLength();
        assertTrue(criticalPath > 0, "Critical path length should be positive");
    }

    @Test
    public void testMetricsCollection() {
        DAGShortestPath sp = new DAGShortestPath(simpleDAG, 0, metrics);
        sp.computeShortestPaths();

        assertTrue(metrics.getOperationCount("Edge relaxations") > 0, "Should count edge relaxations");
        assertTrue(metrics.getTimeMs("DAG Shortest Path") >= 0, "Should measure time");
    }

    @Test
    public void testUnreachableVertices() {
        Graph disconnectedDAG = new Graph(4, true);
        disconnectedDAG.addEdge(0, 1, 1);
        disconnectedDAG.addEdge(2, 3, 1);

        DAGShortestPath sp = new DAGShortestPath(disconnectedDAG, 0, metrics);
        sp.computeShortestPaths();

        assertEquals(0, sp.getDistance(0));
        assertEquals(1, sp.getDistance(1));
        assertEquals(Long.MAX_VALUE, sp.getDistance(2), "Vertex 2 should be unreachable");
        assertEquals(Long.MAX_VALUE, sp.getDistance(3), "Vertex 3 should be unreachable");
    }
}
