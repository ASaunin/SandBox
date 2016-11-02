package concurrency;

import lombok.AllArgsConstructor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static utils.ThreadUtils.sleep;

public class LatchExample {

    private static CountDownLatch latch = new CountDownLatch(3);

    @AllArgsConstructor
    private static class Runner implements Runnable {

        private int id;

        @Override
        public void run() {
            System.out.printf("Runner " + id + " awaits\n");
            try {
                latch.await();
                System.out.printf("Runner " + id + " runs\n");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        final int NUMBER_OF_RUNNERS = 5;
        final ExecutorService executorService = Executors.newCachedThreadPool();

        IntStream.range(0, NUMBER_OF_RUNNERS)
                .mapToObj(Runner::new)
                .forEach(executorService::execute);

        sleep(1000);
        System.out.println("Ready...");
        latch.countDown();
        sleep(1000);
        System.out.println("Steady...");
        latch.countDown();
        sleep(1000);
        System.out.println("Go!!!");
        latch.countDown();
    }

}
