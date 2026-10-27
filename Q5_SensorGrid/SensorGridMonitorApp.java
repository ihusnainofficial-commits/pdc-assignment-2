import java.util.Random;

public class SensorGridMonitorApp {
    public static void main(String[] args) throws InterruptedException {
        int size = 50000;
        int workers = 4;
        double[] sensorData = new double[size];
        Random random = new Random(42);
        for (int i = 0; i < size; i++) {
            sensorData[i] = 10.0 + random.nextDouble() * 90.0;
        }

        int segment = size / workers;
        double[] segmentMins = new double[workers];
        double[] segmentMaxs = new double[workers];
        Thread[] threads = new Thread[workers];

        long parallelStart = System.nanoTime();
        for (int w = 0; w < workers; w++) {
            final int worker = w;
            final int start = w * segment;
            final int end = (w == workers - 1) ? size : (w + 1) * segment;
            threads[w] = new Thread(() -> {
                ParallelMinSearch minSearch = new ParallelMinSearch(sensorData, start, end);
                ParallelMaxSearch maxSearch = new ParallelMaxSearch(sensorData, start, end);
                segmentMins[worker] = minSearch.processGrid();
                segmentMaxs[worker] = maxSearch.processGrid();
                System.out.println("Worker " + worker + " segment [" + start + ", " + end
                        + ") min=" + segmentMins[worker] + " max=" + segmentMaxs[worker]);
            }, "Sensor-Worker-" + w);
            threads[w].start();
        }
        for (Thread t : threads) {
            t.join();
        }
        long parallelTimeMs = (System.nanoTime() - parallelStart) / 1_000_000;

        double globalMin = segmentMins[0];
        double globalMax = segmentMaxs[0];
        for (int w = 1; w < workers; w++) {
            if (segmentMins[w] < globalMin) {
                globalMin = segmentMins[w];
            }
            if (segmentMaxs[w] > globalMax) {
                globalMax = segmentMaxs[w];
            }
        }

        double serialMin = sensorData[0];
        double serialMax = sensorData[0];
        for (double value : sensorData) {
            if (value < serialMin) {
                serialMin = value;
            }
            if (value > serialMax) {
                serialMax = value;
            }
        }

        System.out.println("=== Sensor Grid Monitor (50,000 readings, 4 workers) ===");
        System.out.println("Global minimum temperature : " + globalMin);
        System.out.println("Global maximum vibration   : " + globalMax);
        System.out.println("Serial check min / max     : " + serialMin + " / " + serialMax);
        System.out.println("Parallel results match serial: "
                + (globalMin == serialMin && globalMax == serialMax));
        System.out.println("Parallel execution time: " + parallelTimeMs + " ms");
    }
}
