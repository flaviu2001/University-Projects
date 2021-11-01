import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {
    public static Integer a, b, c;
    public static Integer functionType; //0 - task, 1 - pool
    public static Integer scanType; // 0 - row, 1 - column, 2 - kth
    public static Integer taskNumber;
    public static Matrix m1;
    public static Matrix m2;

    public static void getParams() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("a: ");
        a = scanner.nextInt();
        System.out.print("b: ");
        b = scanner.nextInt();
        System.out.print("c: ");
        c = scanner.nextInt();
        System.out.print("function type: ");
        functionType = scanner.nextInt();
        System.out.print("scan type: ");
        scanType = scanner.nextInt();
        System.out.print("task number: ");
        taskNumber = scanner.nextInt();
        genMatrix();
    }

    public static void genMatrix() {
        m1 = new Matrix(a, b);
        m2 = new Matrix(b, c);
    }

    public static void hardCodeParams() {
        a = 3;
        b = 2;
        c = 2;
        functionType = 0;
        scanType = 0;
        m1 = new Matrix(new Integer[][]{
                {1, 2},
                {3, 4},
                {5, 6}
        });
        m2 = new Matrix(new Integer[][]{
                {7, 8},
                {9, 10}
        });
        taskNumber = 3;
    }

    public static Matrix productByTasks() throws InterruptedException {
        Integer[][] ans = new Integer[a][c];
        List<Thread> threads = new ArrayList<>();
        int iterations = a*c/taskNumber;
        for (int i = 0; i < taskNumber; ++i) {
            int left = i * iterations;
            int right = Math.min((i+1) * iterations, a*c);
            if (functionType == 0)
                threads.add(new Thread(new RowThread(ans, left, right)));
            else if (functionType == 1)
                threads.add(new Thread(new ColumnThread(ans, left, right)));
            else threads.add(new Thread(new KthThread(ans, i, taskNumber)));
        }
        for (Thread thread : threads)
            thread.start();
        for (Thread thread : threads)
            thread.join();
        return new Matrix(ans);
    }

    public static Matrix productByThreadPool() throws InterruptedException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(taskNumber);
        Integer[][] ans = new Integer[a][c];
        List<Runnable> tasks = new ArrayList<>();
        int iterations = a*c/taskNumber;
        for (int i = 0; i < taskNumber; ++i) {
            int left = i * iterations;
            int right = Math.min((i+1) * iterations, a*c);
            if (functionType == 0)
                tasks.add(new Thread(new RowThread(ans, left, right)));
            else if (functionType == 1)
                tasks.add(new Thread(new ColumnThread(ans, left, right)));
            else tasks.add(new Thread(new KthThread(ans, i, taskNumber)));
        }
        for (Runnable task : tasks)
            executor.execute(task);
        executor.shutdown();
        while (!executor.awaitTermination(1, TimeUnit.DAYS)){
            System.out.println("How on earth");
        }
        return new Matrix(ans);
    }

    public static void main(String[] args) throws InterruptedException {
//        hardCodeParams();
        getParams();
        Matrix trueProduct = Matrix.getProductSequentially(m1, m2);
        Matrix computedProduct = null;
        if (functionType == 0)
            computedProduct = productByTasks();
        else computedProduct = productByThreadPool();
        if (trueProduct.equals(computedProduct))
            System.out.println("ok");
        else System.out.println("not ok");
    }
}
