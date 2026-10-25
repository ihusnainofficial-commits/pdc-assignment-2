public class ReutersQuoteEvent extends TradeEvent {
    private double bidPrice;
    private double askPrice;

    public ReutersQuoteEvent(String senderId, long timestamp, double basePrice,
                             double bidPrice, double askPrice) {
        super(senderId, timestamp, basePrice);
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
    }

    @Override
    public void dispatch() {
        double spread = askPrice - bidPrice;
        double midPrice = (bidPrice + askPrice) / 2.0;
        double spreadPercent = (spread / midPrice) * 100.0;
        System.out.println("[ReutersQuoteEvent] Real-time bid-ask market analytics");
        System.out.println("  Sender    : " + senderId);
        System.out.println("  Timestamp : " + timestamp);
        System.out.println("  Bid price : " + bidPrice);
        System.out.println("  Ask price : " + askPrice);
        System.out.println("  Spread    : " + spread);
        System.out.println("  Mid price : " + midPrice);
        System.out.println("  Spread %  : " + spreadPercent);
    }
}
