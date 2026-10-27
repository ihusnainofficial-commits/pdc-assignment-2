public class ParallelMaxSearch extends SensorSearchEngine implements Runnable {

    private double result = Double.MIN_VALUE;

    public ParallelMaxSearch(double[] sensorData, int startIdx, int endIdx) {
        super(sensorData, startIdx, endIdx);
    }
}
