import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("vectorSize: ");
        int vectorSize = scanner.nextInt();
        List<Integer> firstVector = new ArrayList<>();
        List<Integer> secondVector = new ArrayList<>();
        Random random = new Random();
        for(int i = 0; i < vectorSize; ++i) {
            firstVector.add(random.nextInt(100));
            secondVector.add(random.nextInt(100));
        }
        System.out.println("First vector: " + firstVector);
        System.out.println("Second vector: " + secondVector);
        int sharedDateSize = vectorSize / 2;
        Queue<Integer> sharedData = new LinkedList<>();
        Lock mutex = new ReentrantLock();
        Condition condition = mutex.newCondition();
        Thread producer = new Producer(sharedDateSize, sharedData, mutex, condition, firstVector, secondVector);
        Consumer consumer = new Consumer(sharedData, mutex, condition, vectorSize);
        producer.start();
        consumer.start();
        try {
            producer.join();
            consumer.join();
        } catch(InterruptedException ie) {
            System.out.println("Main: " + ie.getMessage());
        }
        System.out.println("Threaded sum: " + consumer.getSum());
        int sum = 0;
        for(int i = 0; i < vectorSize; ++i)
            sum += firstVector.get(i) * secondVector.get(i);
        System.out.println("Correct sum: " + sum);
    }
}
