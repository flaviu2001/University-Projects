import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Consumer extends Thread {
    private final Queue<Integer> queue;
    private final Lock mutex;
    private final Condition condition;
    private final int iterations;
    private int sum;

    public int getSum() {
        return this.sum;
    }

    public Consumer(Queue<Integer> queue, Lock mutex, Condition condition,
                    int iterations) {
        this.queue = queue;
        this.mutex = mutex;
        this.condition = condition;
        this.iterations = iterations;
        this.sum = 0;
    }

    @Override
    public void run() {
        for (int i = 0; i < iterations; ++i)
            this.sum += getProduct();
        System.out.println("Consumer consumed");
    }

    public int getProduct() {
        this.mutex.lock();
        try {
            while (this.queue.isEmpty()) {
                System.out.println("The queue is empty. Consumer is waiting");
                this.condition.await();
            }
            int product = this.queue.remove();
            System.out.println("Consumer gets from queue the product " + product);
            this.condition.signalAll();
            return product;
        } catch (InterruptedException ie) {
            System.out.println("Consumer: " + ie.getMessage());
            return 0;
        } finally {
            this.mutex.unlock();
        }
    }
}
