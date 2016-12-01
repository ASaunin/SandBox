package model;

import lombok.AllArgsConstructor;
import utils.ThreadUtils;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@AllArgsConstructor
public class Philosopher implements Runnable {

    private static final int NUMBER_OF_PHILOSOPHERS = 5;
    private static final long MIN_OPERATION_TIMEOUT = 500;
    private static final long MAX_OPERATION_TIMEOUT = 2000;
    private static final long CHOPSTICK_HOLDING_TIMEOUT = 1000;

    private int id;
    private ReentrantLock leftChopstick;
    private ReentrantLock rightChopstick;

    @Override
    public void run() {
        while (true) {
            try {
                if (takeLeftChopstick() && takeRightChopstick()) {
                    eat();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                releaseChopsticks();
                think();
            }
        }
    }

    private void eat() {
        System.out.printf("Philosopher %d started eating\n", id);
        ThreadUtils.randomSleep(MIN_OPERATION_TIMEOUT, MAX_OPERATION_TIMEOUT);
        System.out.printf("Philosopher %d finished eating\n", id);
    }

    private void think() {
        System.out.printf("Philosopher %d started thinking\n", id);
        ThreadUtils.randomSleep(MIN_OPERATION_TIMEOUT, MAX_OPERATION_TIMEOUT);
        System.out.printf("Philosopher %d finished thinking\n", id);
    }

    private void releaseChopsticks() {
        if (leftChopstick.isHeldByCurrentThread()) {
            leftChopstick.unlock();
            System.out.printf("Philosopher %d released left chopstick\n", id);
        }
        if (rightChopstick.isHeldByCurrentThread()) {
            rightChopstick.unlock();
            System.out.printf("Philosopher %d released right chopstick\n", id);
        }
    }

    private boolean takeLeftChopstick() throws InterruptedException {
        if (leftChopstick.tryLock(CHOPSTICK_HOLDING_TIMEOUT, TimeUnit.MILLISECONDS)) {
            System.out.printf("Philosopher %d took left chopstick\n", id);
            return true;
        }
        return false;
    }

    private boolean takeRightChopstick() throws InterruptedException {
        if (rightChopstick.tryLock(CHOPSTICK_HOLDING_TIMEOUT, TimeUnit.MILLISECONDS)) {
            System.out.printf("Philosopher %d took right chopstick\n", id);
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        final ExecutorService executor = Executors.newCachedThreadPool();
        final ReentrantLock[] chopsticks = Stream
                .generate(ReentrantLock::new)
                .limit(NUMBER_OF_PHILOSOPHERS)
                .toArray(ReentrantLock[]::new);
        final Philosopher[] philosophers = IntStream.range(0, NUMBER_OF_PHILOSOPHERS)
                .mapToObj(i -> new Philosopher(i, chopsticks[i], chopsticks[(i + 1) % NUMBER_OF_PHILOSOPHERS]))
                .toArray(Philosopher[]::new);

        Arrays.stream(philosophers).forEach(executor::execute);
    }

}
