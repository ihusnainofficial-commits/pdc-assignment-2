public class TelemetryThread extends Thread {
    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            Runtime runtime = Runtime.getRuntime();
            long freeMb = runtime.freeMemory() / (1024 * 1024);
            long totalMb = runtime.totalMemory() / (1024 * 1024);
            System.out.println("[Telemetry] Health update " + i
                    + " | Thread: " + Thread.currentThread().getName()
                    + " | CPU: OK | Memory free: " + freeMb
                    + " MB / " + totalMb + " MB");
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                System.out.println("[Telemetry] Interrupted on "
                        + Thread.currentThread().getName());
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("[Telemetry] Finished on "
                + Thread.currentThread().getName());
    }
}
