public class FIXOrderEvent extends TradeEvent {
    private String clOrdId;
    private static final double FEE_RATE = 0.0015;

    public FIXOrderEvent(String senderId, long timestamp, double basePrice, String clOrdId) {
        super(senderId, timestamp, basePrice);
        this.clOrdId = clOrdId;
    }

    @Override
    public void dispatch() {
        double fee = basePrice * FEE_RATE;
        double total = basePrice + fee;
        System.out.println("[FIXOrderEvent] Stock purchase request executed");
        System.out.println("  Sender    : " + senderId);
        System.out.println("  ClOrdId   : " + clOrdId);
        System.out.println("  Timestamp : " + timestamp);
        System.out.println("  Order value: " + basePrice);
        System.out.println("  Fee (0.15%): " + fee);
        System.out.println("  Total payable: " + total);
    }
}
