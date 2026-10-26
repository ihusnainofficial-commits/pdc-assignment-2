public class AsyncWebEngineApp {
    public static void main(String[] args) {
        System.out.println("Default JVM thread name: "
                + Thread.currentThread().getName());

        TelemetryThread telemetryThread = new TelemetryThread();
        telemetryThread.setName("Telemetry-Worker");

        Thread logThread = new Thread(new LogProcessorTask(), "Log-Worker");

        System.out.println("Starting Telemetry-Worker and Log-Worker concurrently...");
        telemetryThread.start();
        logThread.start();

        System.out.println("Telemetry-Worker alive: " + telemetryThread.isAlive());
        System.out.println("Log-Worker alive: " + logThread.isAlive());

        try {
            telemetryThread.join();
            logThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Both workers finished. Main thread: "
                + Thread.currentThread().getName());
    }
}
