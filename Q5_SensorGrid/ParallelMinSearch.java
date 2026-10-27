public class ParallelMinSearch extends SensorSearchEngine implements Runnable {
    private double result = Double.MAX_VALUE;

    public ParallelMinSearch(double[] sensorData, int startIdx, int endIdx) {
        super(sensorData, startIdx, endIdx);
    }

    @Override
    public double processGrid() {
        double min = sensorData[startIdx];
        for (int i = startIdx + 1; i < endIdx; i++) {
            if (sensorData[i] < min) {
                min = sensorData[i];
            }
        }
        result = min;
        return min;
    }

    @Override
    public void run() {
        processGrid();
    }

    public double getResult() {
        return result;
    }
}
