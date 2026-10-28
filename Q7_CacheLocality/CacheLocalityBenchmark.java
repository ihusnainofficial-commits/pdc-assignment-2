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
}
