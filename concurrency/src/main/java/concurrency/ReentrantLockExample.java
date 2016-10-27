package concurrency;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockExample {

    static class Sum {
        final static int TOTAL_AMOUNT = 100;
        private static int x = 0;
        private static int y = TOTAL_AMOUNT;
        private static Lock mutex = new ReentrantLock();

        static int change(int value) {
            mutex.lock();
            try {
                x += value;
                y -= value;
                return x + y;
            } finally {
                mutex.unlock();
            }
        }

        public static void print() {
            System.out.printf("Task proceed: %d+%d=%d\n", x, y, x + y); //Here might be unexpected sum because x + y aren't synchronized
        }

        static int change() {
            final int value = ThreadLocalRandom.current().nextInt();
            return change(value);
        }
    }

}
