public class RequestQueue {

    private char[] data;
    private int front;
    private int rear;
    private int size;

    public RequestQueue(int capacity) {
        data = new char[capacity];
        front = 0;
        rear = -1;
        size = 0;
    }
}
