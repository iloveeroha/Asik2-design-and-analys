package org.example;

import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.io.FileWriter;
import java.io.IOException;

public class BenchmarkRunner {
    private static int[] generateRandomIntArray(int n, int bound) {
        Random rnd = new Random(42);
        return IntStream.range(0, n).map(i -> rnd.nextInt(bound)).toArray();
    }

    private static Integer[] toIntegerArray(int[] a) {
        Integer[] out = new Integer[a.length];
        for (int i = 0; i < a.length; i++) out[i] = a[i];
        return out;
    }

    private static void runOnce(int n, String outCsv) {
        int[] data = generateRandomIntArray(n, Math.max(10, n));
        Integer[] boxed = toIntegerArray(data);
        MinHeap<Integer> heap = MinHeap.buildHeap(boxed);
        long start = System.nanoTime();
        for (int i = 0; i < Math.max(1, n / 10); i++) {
            heap.insert(i);
        }
        while (!heap.isEmpty()) heap.extractMin();
        long end = System.nanoTime();
        long elapsedUs = (end - start) / 1000;
        System.out.println("n=" + n + " elapsed(us)=" + elapsedUs + " metrics=" + heap.getMetrics().csvRow());
        if (outCsv != null) {
            try (FileWriter fw = new FileWriter(outCsv, true)) {
                fw.write(n + "," + elapsedUs + "," + heap.getMetrics().csvRow() + "\\n");
            } catch (IOException e) {
                System.err.println("failed to write csv: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter sizes separated by spaces (e.g. 100 1000 10000):");
        String line = sc.nextLine();
        String[] parts = line.trim().split("\\\\s+");
        System.out.println("Enter output CSV file path or leave empty:");
        String csv = sc.nextLine().trim();
        if (csv.isEmpty()) csv = null;
        for (String p : parts) {
            try {
                int n = Integer.parseInt(p);
                runOnce(n, csv);
            } catch (NumberFormatException ex) {
                System.err.println("skipping invalid number: " + p);
            }
        }
    }
}
