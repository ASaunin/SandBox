package concurrency;

import utils.ThreadUtils;

import java.util.concurrent.atomic.AtomicInteger;

public class RaceConditionForAtomic {

    private static AtomicInteger count = new AtomicInteger(0);

    private static class Counter implements Runnable {

        @Override
        public void run() {
            while (true) {
                ThreadUtils.sleep(1000);
                final int value = count.incrementAndGet();
                System.out.println(value);
            }
        }
    }

    public static void main(String[] args) {

        new Thread(new Counter()).start();
        new Thread(new Counter()).start();

    }

}
