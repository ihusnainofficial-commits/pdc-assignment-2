public class ParallelMaxSearch extends SensorSearchEngine implements Runnable {
    private double result = Double.MIN_VALUE;

    public ParallelMaxSearch(double[] sensorData, int startIdx, int endIdx) {
        super(sensorData, startIdx, endIdx);
    }

    @Override
    public double processGrid() {
        double max = sensorData[startIdx];
        for (int i = startIdx + 1; i < endIdx; i++) {
            if (sensorData[i] > max) {
                max = sensorData[i];
            }
        }
        result = max;
        return max;
    }

    @Override
    public void run() {
        processGrid();
    }

    public double getResult() {
        return result;
    }
}
