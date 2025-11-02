package kz.edu.daa.graph.metrics;

import java.util.*;

public class SimpleMetrics implements Metrics {
    private final Map<String, Long> operationCounts = new HashMap<>();
    private final Map<String, Long> timeMeasurements = new HashMap<>();

    @Override
    public void incrementOperations(String operation) {
        operationCounts.put(operation, operationCounts.getOrDefault(operation, 0L) + 1);
    }

    @Override
    public void addTime(String phase, long nanoTime) {
        timeMeasurements.put(phase, timeMeasurements.getOrDefault(phase, 0L) + nanoTime);
    }

    @Override
    public long getOperationCount(String operation) {
        return operationCounts.getOrDefault(operation, 0L);
    }

    @Override
    public long getTimeNano(String phase) {
        return timeMeasurements.getOrDefault(phase, 0L);
    }

    @Override
    public double getTimeMs(String phase) {
        return getTimeNano(phase) / 1_000_000.0;
    }

    @Override
    public void reset() {
        operationCounts.clear();
        timeMeasurements.clear();
    }

    @Override
    public void printSummary() {
        System.out.println("\n=== Metrics Summary ===");
        System.out.println("Operations:");
        operationCounts.forEach((op, count) ->
                System.out.printf("  %s: %d\n", op, count)
        );
        System.out.println("Time measurements:");
        timeMeasurements.forEach((phase, nanoTime) ->
                System.out.printf("  %s: %.4f ms\n", phase, nanoTime / 1_000_000.0)
        );
    }
}
