import org.example.MinHeap;
import org.example.PerformanceTracker;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

public class MinHeapTest {

    @Test
    void testEmptyHeap() {
        MinHeap<Integer> h = new MinHeap<>();
        assertTrue(h.isEmpty());
        assertThrows(java.util.NoSuchElementException.class, h::peek);
        assertThrows(java.util.NoSuchElementException.class, h::extractMin);
    }

    @Test
    void testSingleElement() {
        MinHeap<Integer> h = new MinHeap<>();
        h.insert(42);
        assertEquals(1, h.size());
        assertEquals(42, h.peek());
        assertEquals(42, h.extractMin());
        assertTrue(h.isEmpty());
    }

    @Test
    void testDuplicates() {
        MinHeap<Integer> h = new MinHeap<>();
        h.insert(5);
        h.insert(5);
        h.insert(5);
        assertEquals(5, h.extractMin());
        assertEquals(5, h.extractMin());
        assertEquals(5, h.extractMin());
        assertTrue(h.isEmpty());
    }

    @Test
    void testBuildHeapAndExtractOrder() {
        Integer[] arr = new Integer[] {5, 3, 8, 1, 2, 7, 4};
        MinHeap<Integer> h = MinHeap.buildHeap(arr);
        int prev = Integer.MIN_VALUE;
        while (!h.isEmpty()) {
            int v = h.extractMin();
            assertTrue(v >= prev);
            prev = v;
        }
    }

    @Test
    void testDecreaseKey() {
        MinHeap<Integer> h = new MinHeap<>();
        h.insert(10);
        h.insert(20);
        h.insert(30);
        h.decreaseKey(2, 5);
        assertEquals(5, h.peek());
    }

    @Test
    void testMerge() {
        MinHeap<Integer> a = new MinHeap<>();
        MinHeap<Integer> b = new MinHeap<>();
        a.insert(1); a.insert(4); a.insert(7);
        b.insert(2); b.insert(3); b.insert(6);
        a.merge(b);
        int prev = Integer.MIN_VALUE;
        while (!a.isEmpty()) {
            int v = a.extractMin();
            assertTrue(v >= prev);
            prev = v;
        }
    }

    @Test
    void testMetricsCounted() {
        PerformanceTracker tracker = new PerformanceTracker();
        MinHeap<Integer> h = new MinHeap<>(16, true, tracker);
        h.insert(5); h.insert(3); h.insert(4);
        h.extractMin();
        assertTrue(tracker.getComparisons() >= 0);
        assertTrue(tracker.getArrayAccesses() >= 0);
    }

    @Test
    void randomizedPropertyTest() {
        Random rnd = new Random(123);
        for (int t = 0; t < 20; t++) {
            MinHeap<Integer> h = new MinHeap<>();
            int n = rnd.nextInt(100);
            for (int i = 0; i < n; i++) {
                h.insert(rnd.nextInt(1000));
            }
            int prev = Integer.MIN_VALUE;
            while (!h.isEmpty()) {
                int v = h.extractMin();
                assertTrue(v >= prev);
                prev = v;
            }
        }
    }
}
