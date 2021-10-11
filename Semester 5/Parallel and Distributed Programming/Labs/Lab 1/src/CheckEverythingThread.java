public record CheckEverythingThread(Supermarket supermarket, int threadNumber) implements Runnable {
    @Override
    public void run() {
        System.out.println("Thread " + threadNumber + " is checking if the supermarket is ok");
        boolean ok = supermarket.checkEverything();
        if (ok)
            System.out.println("ok!");
        else System.out.println("not ok :/");
    }
}