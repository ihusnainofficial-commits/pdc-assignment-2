public class PacketSymmetryAuditor {

    public static boolean isSymmetric(String payload) {
        TransactionStack stack = new TransactionStack(payload.length());
        RequestQueue queue = new RequestQueue(payload.length());

        for (int i = 0; i < payload.length(); i++) {
            char c = payload.charAt(i);
            stack.push(c);
            queue.enqueue(c);
        }

        while (!stack.isEmpty() && !queue.isEmpty()) {
            char fromStack = stack.pop();
            char fromQueue = queue.dequeue();
            if (fromStack != fromQueue) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        String[] payloads = {"ABCCBA", "LEVEL", "PACKET", "RADAR"};

        System.out.println("=== Packet Symmetry Audit (Stack LIFO vs Queue FIFO) ===");
        for (String payload : payloads) {
            boolean symmetric = isSymmetric(payload);
            System.out.println("Payload: " + payload
                    + "  ->  " + (symmetric
                            ? "PALINDROME - bi-directional symmetry VERIFIED"
                            : "NOT PALINDROME - symmetry FAILED"));
        }
    }
}
