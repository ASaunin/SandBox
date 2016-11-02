package concurrency;

import utils.ThreadUtils;

public class RaceCondition {

    private static int count = 0;

    private static class Counter implements Runnable {

        @Override
        public void run() {
            while (true) {
                ThreadUtils.sleep(1000);
                inc();
            }
        }

        //Doesn't work without static
        //Atomics are preferable in that case, cause they cost less resources
        synchronized private static void inc() {
            count++;
            System.out.println(count);
        }

    }

    public static void main(String[] args) {

        new Thread(new Counter()).start();
        new Thread(new Counter()).start();

    }

}
