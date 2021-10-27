import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Producer extends Thread {
    private final int queueSize;
    private final Queue<Integer> queue;
    private final Lock mutex;
    private final Condition condition;
    private final List<Integer> firstVector;
    private final List<Integer> secondVector;

    public Producer(int size, Queue<Integer> queue, Lock mutex, Condition condition,
                    List<Integer> firstVector, List<Integer> secondVector) {
        this.queueSize = size;
        this.queue = queue;
        this.mutex = mutex;
        this.condition = condition;
        this.firstVector = firstVector;
        this.secondVector = secondVector;
    }

    @Override
    public void run() {
        for (int i = 0; i < firstVector.size(); ++i)
            putProduct(firstVector.get(i) * secondVector.get(i));
        System.out.println("Producer produced");
    }

    public void putProduct(int product) {
        this.mutex.lock();
        try {
            while (this.queue.size() >= this.queueSize) {
                System.out.println("The queue is full. Producer is waiting. Size: " + this.queueSize);
                this.condition.await();
            }
            System.out.println("Producer adds to queue the product " + product);
            this.queue.add(product);
            this.condition.signalAll();
        } catch (InterruptedException ie) {
            System.out.println("Producer: " + ie.getMessage());
        } finally {
            this.mutex.unlock();
        }
    }
}
