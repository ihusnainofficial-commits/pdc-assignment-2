import java.util.concurrent.RecursiveTask;

public class ParallelRiskCalculator extends RecursiveTask<Double> {

    public static final int THRESHOLD = 1000;
    private double[] assets;
    private int start;
    private int end;
}
