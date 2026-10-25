public class TransactionStack {

    private char[] data;
    private int top;

    public TransactionStack(int capacity) {
        data = new char[capacity];
        top = -1;
    }
}
