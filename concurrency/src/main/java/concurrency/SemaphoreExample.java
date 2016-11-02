package concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import static utils.ThreadUtils.randomSleep;

public class SemaphoreExample {

    static class Skater implements Runnable {

        private final Semaphore semaphore;

        public Skater(Semaphore semaphore) {
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            System.out.printf("New person started skating. Available %d places\n", semaphore.availablePermits());
            randomSleep(1000, 5000);
            semaphore.release();
            System.out.printf("Person finished skating. Available %d places\n", semaphore.availablePermits());
        }

    }

    public static void main(String[] args) {
        final ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(10);

        while (true) {
            randomSleep(500, 1000);
            semaphore.acquireUninterruptibly();
            executorService.execute(new Skater(semaphore));
        }
    }

}
