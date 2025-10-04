package org.example;

import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;

public class MinHeap<T extends Comparable<T>> {
    private T[] heap;
    private int size;
    private final PerformanceTracker metrics;
    private final boolean track;
    private static final int DEFAULT_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public MinHeap() {
        this.heap = (T[]) new Comparable[DEFAULT_CAPACITY];
        this.size = 0;
        this.metrics = new PerformanceTracker();
        this.track = true;
        metrics.incAllocations(1);
    }

    @SuppressWarnings("unchecked")
    public MinHeap(int capacity, boolean trackMetrics, PerformanceTracker tracker) {
        if (capacity <= 0) capacity = DEFAULT_CAPACITY;
        this.heap = (T[]) new Comparable[capacity];
        this.size = 0;
        this.track = trackMetrics;
        this.metrics = tracker == null ? new PerformanceTracker() : tracker;
        if (track) metrics.incAllocations(1);
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
    public PerformanceTracker getMetrics() { return metrics; }

    public void clear() {
        Arrays.fill(heap, 0, size, null);
        if (track && size > 0) metrics.incArrayAccesses(size);
        size = 0;
    }

    public void insert(T value) {
        if (value == null) throw new IllegalArgumentException("null not allowed");
        ensureCapacity(size + 1);
        heap[size] = value;
        if (track) metrics.incArrayAccesses(1);
        siftUp(size);
        size++;
    }

    public T peek() {
        if (isEmpty()) throw new NoSuchElementException("empty");
        if (track) metrics.incArrayAccesses(1);
        return heap[0];
    }

    public T extractMin() {
        if (isEmpty()) throw new NoSuchElementException("empty");
        T min = heap[0];
        if (track) metrics.incArrayAccesses(1);
        size--;
        if (size > 0) {
            heap[0] = heap[size];
            if (track) metrics.incArrayAccesses(1);
            heap[size] = null;
            if (track) metrics.incArrayAccesses(1);
            siftDown(0);
        } else {
            heap[0] = null;
            if (track) metrics.incArrayAccesses(1);
        }
        return min;
    }

    public void decreaseKey(int index, T newValue) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        if (newValue == null) throw new IllegalArgumentException("null not allowed");
        if (track) metrics.incArrayAccesses(1);
        if (heap[index].compareTo(newValue) < 0) throw new IllegalArgumentException("new value greater than current");
        if (track) metrics.incComparisons(1);
        heap[index] = newValue;
        if (track) metrics.incArrayAccesses(1);
        siftUp(index);
    }

    public void merge(MinHeap<T> other) {
        if (other == null || other.size == 0) return;
        ensureCapacity(this.size + other.size);
        System.arraycopy(other.heap, 0, this.heap, this.size, other.size);
        if (track) metrics.incArrayAccesses(other.size);
        this.size += other.size;
        buildHeapInPlace();
    }

    public static <E extends Comparable<E>> MinHeap<E> buildHeap(E[] array) {
        if (array == null) throw new IllegalArgumentException("null array");
        PerformanceTracker tracker = new PerformanceTracker();
        MinHeap<E> h = new MinHeap<>(Math.max(array.length, DEFAULT_CAPACITY), true, tracker);
        if (array.length > 0) {
            System.arraycopy(array, 0, h.heap, 0, array.length);
            tracker.incArrayAccesses(array.length);
            h.size = array.length;
            h.buildHeapInPlace();
        }
        return h;
    }

    @SuppressWarnings("unchecked")
    public static <E extends Comparable<E>> MinHeap<E> fromCollection(Collection<E> data) {
        if (data == null) throw new IllegalArgumentException("null collection");
        E[] arr = (E[]) data.toArray(new Comparable[0]);
        return buildHeap(arr);
    }

    private void buildHeapInPlace() {
        if (size <= 1) return;
        for (int i = parent(size - 1); i >= 0; i--) siftDown(i);
    }

    private void ensureCapacity(int required) {
        if (required <= heap.length) return;
        int newCap = Math.max(required, heap.length << 1);
        heap = Arrays.copyOf(heap, newCap);
        if (track) metrics.incAllocations(1);
    }

    private void siftUp(int i) {
        int cur = i;
        while (cur > 0) {
            int p = parent(cur);
            if (track) metrics.incArrayAccesses(2);
            metrics.incComparisons(1);
            if (heap[cur].compareTo(heap[p]) < 0) {
                swap(cur, p);
                cur = p;
            } else break;
        }
    }

    private void siftDown(int i) {
        int cur = i;
        while (true) {
            int left = leftChild(cur);
            int right = rightChild(cur);
            int smallest = cur;

            if (left < size) {
                if (track) metrics.incArrayAccesses(2);
                metrics.incComparisons(1);
                if (heap[left].compareTo(heap[smallest]) < 0) smallest = left;
            }
            if (right < size) {
                if (track) metrics.incArrayAccesses(2);
                metrics.incComparisons(1);
                if (heap[right].compareTo(heap[smallest]) < 0) smallest = right;
            }
            if (smallest != cur) {
                swap(cur, smallest);
                cur = smallest;
            } else break;
        }
    }

    private void swap(int i, int j) {
        T tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
        if (track) {
            metrics.incSwaps(1);
            metrics.incArrayAccesses(3);
        }
    }

    private static int parent(int i) { return (i - 1) / 2; }
    private static int leftChild(int i) { return 2 * i + 1; }
    private static int rightChild(int i) { return 2 * i + 2; }
}
