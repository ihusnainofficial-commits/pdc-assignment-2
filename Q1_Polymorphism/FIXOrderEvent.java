public class FIXOrderEvent extends TradeEvent {

    private String clOrdId;
    private static final double FEE_RATE = 0.0015;

    public FIXOrderEvent(String senderId, long timestamp, double basePrice, String clOrdId) {
        super(senderId, timestamp, basePrice);
        this.clOrdId = clOrdId;
    }
}
