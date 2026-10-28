public class CacheLocalityBenchmark {
    private static final int N = 2000;
    private static final int ITERATIONS = 10;
    private static double[][] matrix;

    public static double traverseRowMajor() {
        double sum = 0.0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                sum += matrix[i][j];
            }
        }
        return sum;
    }

    public static double traverseColumnMajor() {
        double sum = 0.0;
        for (int j = 0; j < N; j++) {
            for (int i = 0; i < N; i++) {
                sum += matrix[i][j];
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        matrix = new double[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                matrix[i][j] = (i + j) % 100;
            }
        }

        traverseRowMajor();
        traverseColumnMajor();

        System.out.println("=== Cache Locality Benchmark: 2000 x 2000 doubles ===");
        double checksum = 0.0;
        long totalRowMs = 0;
        long totalColMs = 0;

        for (int iter = 1; iter <= ITERATIONS; iter++) {
            long rowStart = System.nanoTime();
            double rowSum = traverseRowMajor();
            long rowMs = (System.nanoTime() - rowStart) / 1_000_000;

            long colStart = System.nanoTime();
            double colSum = traverseColumnMajor();
            long colMs = (System.nanoTime() - colStart) / 1_000_000;

            checksum += rowSum + colSum;
            totalRowMs += rowMs;
            totalColMs += colMs;
            System.out.println("Iteration " + iter
                    + ": Row-Major = " + rowMs + " ms"
                    + " | Column-Major = " + colMs + " ms"
                    + " | Sums equal: " + (rowSum == colSum));
        }

        double avgRow = totalRowMs / (double) ITERATIONS;
        double avgCol = totalColMs / (double) ITERATIONS;
        double speedup = avgCol / avgRow;

        System.out.println("Average Row-Major time   : " + avgRow + " ms");
        System.out.println("Average Column-Major time: " + avgCol + " ms");
        System.out.println("Speedup (Column / Row)   : " + speedup);
        System.out.println("Checksum (prevents dead-code elimination): " + checksum);
        System.out.println("Conclusion: Row-Major is faster because it respects");
        System.out.println("spatial locality - one cache line serves many");
        System.out.println("neighbouring elements, while Column-Major wastes");
        System.out.println("a cache line on almost every single access.");
    }
}
