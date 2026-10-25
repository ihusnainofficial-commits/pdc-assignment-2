public class TradingEngineApp {
    public static void main(String[] args) {
        long now = System.currentTimeMillis();

        TradeEvent fixOrder = new FIXOrderEvent("FIX-GATEWAY-01", now, 100000.0, "ORD-1001");
        TradeEvent reutersQuote = new ReutersQuoteEvent("REUTERS-01", now, 150.0, 149.80, 150.20);

        System.out.println("=== Trading Engine: Polymorphic Dispatch ===");
        fixOrder.dispatch();
        System.out.println();
        reutersQuote.dispatch();
        System.out.println();
        System.out.println("Note: both calls used a TradeEvent reference,");
        System.out.println("but each object ran its own overridden dispatch().");
    }
}
