package executors;

import utils.ThreadUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ThreadPoolManagerApp {

    private static class Task implements Runnable {

        private String name;
        private long minMillis;
        private long maxMillis;

        private Task(long minMillis, long maxMillis, String name) {
            this.minMillis = minMillis;
            this.maxMillis = maxMillis;
            this.name = name;
        }

        @Override
        public void run() {
            final Thread currentThread = Thread.currentThread();
            currentThread.setName(name);
            System.out.printf("%s started work\n", name);
            ThreadUtils.randomSleep(minMillis, maxMillis);
            System.out.printf("%s finished work\n", name);
        }

        public String getName() {
            return name;
        }

    }

    public static void main(String[] args) {
        final ThreadPoolManager manager = ThreadPoolManager.getInstance();
        final ExecutorService executor = Executors.newFixedThreadPool(20);
        final int N_TASKS = 20;
        final int MIN_TIMEOUT = 100;
        final int MAX_TIMEOUT = 500;

        IntStream.range(0, N_TASKS)
                .mapToObj((i) ->
                        (Runnable) () -> {
                            ThreadUtils.randomSleep(MIN_TIMEOUT, MAX_TIMEOUT);
                            if (ThreadLocalRandom.current().nextInt(2) == 0)
                                manager.submitReadTask(new Task(MIN_TIMEOUT, MAX_TIMEOUT, "Read" + i));
                            else
                                manager.submitWriteTask(new Task(MIN_TIMEOUT, MAX_TIMEOUT, "Write" + i));
                        }
                )
                .limit(N_TASKS)
                .forEach(executor::execute);

        executor.shutdown();
    }

}
