public class LogProcessorTask implements Runnable {
    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            System.out.println("[LogProcessor] Iteration " + i
                    + " | Thread: " + Thread.currentThread().getName()
                    + " | Log metrics processed: " + (i * 120) + " entries");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("[LogProcessor] Interrupted on "
                        + Thread.currentThread().getName());
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("[LogProcessor] Finished on "
                + Thread.currentThread().getName());
    }
}
