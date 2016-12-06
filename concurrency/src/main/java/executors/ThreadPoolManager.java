package executors;

import utils.ThreadUtils;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class ThreadPoolManager {

    private static final ThreadPoolManager INSTANCE = new ThreadPoolManager();
    private static final ExecutorService executor = newFixedThreadPoolWithArrayBlockingQueue();
    private final Semaphore writeLock = new Semaphore(0);
    private final Semaphore readLock = new Semaphore(0);

    private static int readersCount = 0;

    public static ThreadPoolManager getInstance() {
        return INSTANCE;
    }

    public void submitWriteTask(Task task) {
        System.out.printf("%s requests for task %s\n", Thread.currentThread(), task.getName());
        CompletableFuture.runAsync(task, executor)
                .whenComplete((value, exception) -> {
                });
    }

    public void submitReadTask(Task task) {
        System.out.printf("%s requests for task %s\n", Thread.currentThread(), task.getName());
        CompletableFuture.runAsync(task, executor)
                .whenComplete((value, exception) -> {
                });
    }

    private static ThreadPoolExecutor newFixedThreadPoolWithArrayBlockingQueue() {
        final int corePoolSize = 10;
        final int maxPoolSize = 20;
        final long keepAliveTime = 10;
        final int queueSize = 10;

        return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.MINUTES, new ArrayBlockingQueue<>(queueSize));
    }

    public static void main(String[] args) {
        final int N_TASKS = 20;
        final int MIN_TIMEOUT = 1000;
        final int MAX_TIMEOUT = 20000;

        List<Task> readTasks = IntStream.range(0, N_TASKS)
                .mapToObj((i) -> new Task(MIN_TIMEOUT, MAX_TIMEOUT, "Read" + i))
                .limit(N_TASKS)
                .collect(toList());

        List<Task> writeTasks = IntStream.range(0, N_TASKS)
                .mapToObj((i) -> new Task(MIN_TIMEOUT, MAX_TIMEOUT, "Write" + i))
                .limit(N_TASKS)
                .collect(toList());

        ThreadPoolManager manager = getInstance();

        manager.submitReadTask(readTasks.get(0));
        ThreadUtils.sleep(10);
        manager.submitReadTask(readTasks.get(1));
        ThreadUtils.sleep(10);
        manager.submitWriteTask(writeTasks.get(0));
        ThreadUtils.sleep(10);
        manager.submitReadTask(readTasks.get(2));
        ThreadUtils.sleep(10);

        executor.shutdown();

    }

    static class Task implements Runnable {

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
            System.out.printf("Thread %s started work\n", name);
            ThreadUtils.randomSleep(minMillis, maxMillis);
            System.out.printf("Thread %s finished work\n", name);

        }

        public String getName() {
            return name;
        }
    }

}
