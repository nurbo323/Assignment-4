package kz.edu.daa.graph.metrics;

public interface Metrics {
    void incrementOperations(String operation);
    void addTime(String phase, long nanoTime);
    long getOperationCount(String operation);
    long getTimeNano(String phase);
    double getTimeMs(String phase);
    void reset();
    void printSummary();
}
