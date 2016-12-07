package executors;

import java.util.concurrent.*;

public class ThreadPoolManager {

    private static final ThreadPoolManager INSTANCE = new ThreadPoolManager();
    private static final ExecutorService executor = newFixedThreadPoolWithArrayBlockingQueue();
    private static final SubscriberQueue readerQueue = new SubscriberQueue();
    private static final SubscriberQueue writerQueue = new SubscriberQueue();
//    private static final LinkedBlockingQueue<Runnable> bufferQueue = new LinkedBlockingQueue<>();

    public static ThreadPoolManager getInstance() {
        return INSTANCE;
    }

    public void submitWriteTask(Runnable task) {
        System.out.printf("%s requests to write\n", task);
        if (readerQueue.hasItems() || writerQueue.hasItems()) {
//            bufferQueue.offer(task);
            writerQueue.put();
            System.out.printf("%s locked\n", task);
            writerQueue.lock();
            System.out.printf("%s unlocked\n", task);
        } else {
            writerQueue.put();
        }
        CompletableFuture.runAsync(task, executor)
                .whenComplete((value, exception) -> {
                    writerQueue.get();
                    if (writerQueue.hasItems()) {
                        writerQueue.unlock(); //Release single write tasks (could start simultaneously)
                    } else {
                        readerQueue.unlockAll(); //Release all read tasks (could start in parallel)
                    }

                });
    }

    public void submitReadTask(Runnable task) {
        System.out.printf("%s requests to read\n", task);
        if (writerQueue.hasItems()) {
//            bufferQueue.offer(task);
            readerQueue.put();
            System.out.printf("%s locked\n", task);
            readerQueue.lock();
            System.out.printf("%s unlocked\n", task);
        } else {
            readerQueue.put();
        }
        CompletableFuture.runAsync(task, executor)
                .whenComplete((value, exception) -> {
                    readerQueue.get();
                    if (readerQueue.isEmpty()) {
                        writerQueue.unlock(); //Release single write tasks (could start simultaneously)
                    }
                });
    }

    public static ThreadPoolExecutor newFixedThreadPoolWithArrayBlockingQueue() {
        final int corePoolSize = 10;
        final int maxPoolSize = 20;
        final long keepAliveTime = 10;
        final int queueSize = 10;

        return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.MINUTES, new ArrayBlockingQueue<>(queueSize));
    }

//    public static void main(String[] args) {
//        final int N_TASKS = 20;
//        final int MIN_TIMEOUT = 1000;
//        final int MAX_TIMEOUT = 5000;
//
//        List<Task> readTasks = IntStream.range(0, N_TASKS)
//                .mapToObj((i) -> new Task(MIN_TIMEOUT, MAX_TIMEOUT, "Read" + i))
//                .limit(N_TASKS)
//                .collect(toList());
//
//        List<Task> writeTasks = IntStream.range(0, N_TASKS)
//                .mapToObj((i) -> new Task(MIN_TIMEOUT, MAX_TIMEOUT, "Write" + i))
//                .limit(N_TASKS)
//                .collect(toList());
//
//        ThreadPoolManager manager = getInstance();
//
//        manager.submitReadTask(readTasks.get(0));
//        manager.submitReadTask(readTasks.get(1));
//        manager.submitWriteTask(writeTasks.get(0));
//        manager.submitReadTask(readTasks.get(2));
//        manager.submitReadTask(readTasks.get(3));
//        manager.submitReadTask(readTasks.get(4));
//        manager.submitWriteTask(writeTasks.get(2));
//        manager.submitWriteTask(writeTasks.get(3));
//        manager.submitReadTask(readTasks.get(5));
//        manager.submitReadTask(readTasks.get(6));
//
//        executor.shutdown();
//
//    }
//
//    private static class Task implements Runnable {
//
//        private String name;
//        private long minMillis;
//        private long maxMillis;
//
//        private Task(long minMillis, long maxMillis, String name) {
//            this.minMillis = minMillis;
//            this.maxMillis = maxMillis;
//            this.name = name;
//        }
//
//        @Override
//        public void run() {
//            final Thread currentThread = Thread.currentThread();
//            currentThread.setName(name);
//            System.out.printf("%s started work\n", name);
//            ThreadUtils.randomSleep(minMillis, maxMillis);
//            System.out.printf("%s finished work\n", name);
//            if (name.equals("Read3"))
//                throw new UnsupportedOperationException();
//        }
//
//        public String getName() {
//            return name;
//        }
//
//    }

    private static class SubscriberQueue {

        private static final Semaphore semaphore = new Semaphore(0);
        private int count = 0;

        public int getCount() {
            return count;
        }

        public boolean isEmpty() {
            return count == 0;
        }

        public boolean hasItems() {
            return count != 0;
        }

        public void lock() {
            semaphore.acquireUninterruptibly();
        }

        public void lockAll() {
            semaphore.acquireUninterruptibly(count);
        }

        public void unlock() {
            if (count > 0)
                semaphore.release();
        }

        public void unlockAll() {
            semaphore.release(count);
        }

        public void put() {
            count++;
        }

        public void get() {
            count--;
        }

    }

}
