import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class ForkJoinPortfolioApp {
    public static void main(String[] args) {
        int size = 10000;
        double[] assets = new double[size];
        Random random = new Random(42);
        for (int i = 0; i < size; i++) {
            assets[i] = 100.0 + random.nextDouble() * 900.0;
        }

        double serialSum = 0.0;
        for (double value : assets) {
            serialSum += value + Math.log1p(value) * 10.0;
        }
        double parallelSum;
        ForkJoinPool pool = new ForkJoinPool();
        parallelSum = pool.invoke(
                new ParallelRiskCalculator(assets, 0, assets.length));

        int repeats = 200;
        long serialStart = System.nanoTime();
        double serialCheck = 0.0;
        for (int r = 0; r < repeats; r++) {
            double s = 0.0;
            for (double value : assets) {
                s += value + Math.log1p(value) * 10.0;
            }
            serialCheck += s;
        }
        long serialTimeMs = (System.nanoTime() - serialStart) / 1_000_000;

        long parallelStart = System.nanoTime();
        double parallelCheck = 0.0;
        for (int r = 0; r < repeats; r++) {
            parallelCheck += pool.invoke(
                    new ParallelRiskCalculator(assets, 0, assets.length));
        }
        long parallelTimeMs = (System.nanoTime() - parallelStart) / 1_000_000;
        pool.shutdown();

        System.out.println("=== Fork/Join Portfolio Assessment ===");
        System.out.println("Portfolio size : " + size + " assets");
        System.out.println("Serial sum     : " + serialSum);
        System.out.println("Parallel sum   : " + parallelSum);
        System.out.println("Sums match     : " + (Math.abs(serialSum - parallelSum) < 0.001));
        System.out.println("Timing over " + repeats + " repeats:");
        System.out.println("Serial total   : " + serialTimeMs + " ms");
        System.out.println("Parallel total : " + parallelTimeMs + " ms");
        if (parallelTimeMs > 0) {
            System.out.println("Speedup (serial / parallel): "
                    + ((double) serialTimeMs / parallelTimeMs));
        }
        System.out.println("Note: on a small 10,000-element array the parallel");
        System.out.println("gain is small and can vary run to run, because");
        System.out.println("splitting and joining costs time too. The point");
        System.out.println("proven here is identical results via parallel split.");

        double riskCap = 800.0;
        double[] before = assets.clone();
        int aboveCap = 0;
        for (double value : before) {
            if (value > riskCap) {
                aboveCap++;
            }
        }
        ForkJoinPool pool2 = new ForkJoinPool();
        long stressStart = System.nanoTime();
        pool2.invoke(new PortfolioThresholdTask(assets, 0, assets.length, riskCap));
        long stressTimeMs = (System.nanoTime() - stressStart) / 1_000_000;
        pool2.shutdown();

        double beforeSimple = 0.0;
        for (double value : before) {
            beforeSimple += value;
        }
        double afterSum = 0.0;
        for (double value : assets) {
            afterSum += value;
        }
        System.out.println();
        System.out.println("Stress test: risk cap = " + riskCap);
        System.out.println("Assets above cap (5% penalty applied): " + aboveCap);
        System.out.println("Simple total before stress test: " + beforeSimple);
        System.out.println("Simple total after stress test : " + afterSum);
        System.out.println("Stress-test time: " + stressTimeMs + " ms");
    }
}
