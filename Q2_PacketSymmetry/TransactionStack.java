public class TransactionStack {
    private char[] data;
    private int top;

    public TransactionStack(int capacity) {
        data = new char[capacity];
        top = -1;
    }

    public void push(char c) {
        if (top + 1 >= data.length) {
            throw new IllegalStateException("Stack overflow: capacity reached");
        }
        data[++top] = c;
    }

    public char pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack underflow: stack is empty");
        }
        return data[top--];
    }

    public char peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return data[top];
    }

    public boolean isEmpty() {
        return top == -1;
    }
}
