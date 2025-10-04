package org.example;

public class PerformanceTracker {
    private long comparisons;
    private long swaps;
    private long arrayAccesses;
    private long allocations;
    private long recursiveCalls;

    public void incComparisons(long v) { comparisons += v; }
    public void incSwaps(long v) { swaps += v; }
    public void incArrayAccesses(long v) { arrayAccesses += v; }
    public void incAllocations(long v) { allocations += v; }
    public void incRecursiveCalls(long v) { recursiveCalls += v; }

    public long getComparisons() { return comparisons; }
    public long getSwaps() { return swaps; }
    public long getArrayAccesses() { return arrayAccesses; }
    public long getAllocations() { return allocations; }
    public long getRecursiveCalls() { return recursiveCalls; }

    public void reset() {
        comparisons = 0;
        swaps = 0;
        arrayAccesses = 0;
        allocations = 0;
        recursiveCalls = 0;
    }

    public String csvHeader() {
        return "comparisons,swaps,arrayAccesses,allocations,recursiveCalls";
    }

    public String csvRow() {
        return comparisons + "," + swaps + "," + arrayAccesses + "," + allocations + "," + recursiveCalls;
    }
}
