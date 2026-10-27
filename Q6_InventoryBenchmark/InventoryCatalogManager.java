import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InventoryCatalogManager {
    private static final int THREADS = 10;
    private static final int OPERATIONS = 50000;
    private static final int PRODUCTS = 1000;

    private static Map<String, Integer> freshCatalog() {
        Map<String, Integer> catalog = new HashMap<>();
        for (int i = 0; i < PRODUCTS; i++) {
            catalog.put("PROD-" + i, 100);
        }
        return catalog;
    }

    public static void benchmarkSynchronizedMap() throws InterruptedException {
        Map<String, Integer> catalog =
                Collections.synchronizedMap(freshCatalog());
        runBenchmark("Collections.synchronizedMap", catalog, true);
    }

    public static void benchmarkConcurrentMap() throws InterruptedException {
        Map<String, Integer> catalog = new ConcurrentHashMap<>(freshCatalog());
        runBenchmark("ConcurrentHashMap", catalog, false);
    }

    private static void runBenchmark(String name, Map<String, Integer> catalog,
                                     boolean useSynchronizedBlock)
            throws InterruptedException {
        int perThread = OPERATIONS / THREADS;
        ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        CountDownLatch done = new CountDownLatch(THREADS);

        long start = System.nanoTime();
        for (int t = 0; t < THREADS; t++) {
            final int threadId = t;
            pool.submit(() -> {
                for (int i = 0; i < perThread; i++) {
                    String productId = "PROD-" + ((threadId * perThread + i) % PRODUCTS);
                    if (useSynchronizedBlock) {
                        synchronized (catalog) {
                            Integer stock = catalog.get(productId);
                            catalog.put(productId, stock + 1);
                        }
                    } else {
                        catalog.merge(productId, 1, Integer::sum);
                    }
                }
                done.countDown();
            });
        }
        done.await();
        pool.shutdown();
        long elapsedMs = (System.nanoTime() - start) / 1_000_000;
        double seconds = elapsedMs / 1000.0;
        double throughput = seconds > 0 ? OPERATIONS / seconds : OPERATIONS;

        System.out.println("--- " + name + " ---");
        System.out.println("Threads: " + THREADS + " | Operations: " + OPERATIONS);
        System.out.println("Execution time: " + elapsedMs + " ms");
        System.out.println("Throughput: " + (long) throughput + " operations/second");
        System.out.println("Sample stock PROD-0 after run: " + catalog.get("PROD-0"));
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Inventory Catalog Concurrency Benchmark ===");
        benchmarkSynchronizedMap();
        System.out.println();
        benchmarkConcurrentMap();
        System.out.println();
        System.out.println("Reading: synchronizedMap locks the whole table for");
        System.out.println("every update, so threads wait on each other. The");
        System.out.println("concurrent map locks only the bucket being changed,");
        System.out.println("so unrelated products can be updated in parallel.");
    }
}
