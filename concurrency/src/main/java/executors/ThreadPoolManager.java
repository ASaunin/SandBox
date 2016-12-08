package executors;

import java.util.concurrent.*;

public class ThreadPoolManager {

    private static final ThreadPoolManager INSTANCE = new ThreadPoolManager();
    private static final ExecutorService executor = newFixedThreadPoolWithArrayBlockingQueue();
    private static final SubscriberQueue readerQueue = new SubscriberQueue();
    private static final SubscriberQueue writerQueue = new SubscriberQueue();

    public static ThreadPoolManager getInstance() {
        return INSTANCE;
    }

    public void submitWriteTask(Runnable task) {
        System.out.printf("Writer %s requests to write\n", task);
        if (readerQueue.activeIsEmpty() && writerQueue.activeIsEmpty()) {
            writerQueue.putActive(task);
        } else {
            writerQueue.putBuffer(task);
            System.out.printf("Writer %s locked\n", task);
            writerQueue.acquire();
            writerQueue.putActive(writerQueue.pollBuffer());
            System.out.printf("Writer %s unlocked\n", task);
        }
        CompletableFuture.runAsync(task, executor)
                .thenAccept((value) -> {
                    writerQueue.pollActive();
                    handleBufferedTask();
                });
    }

    public void submitReadTask(Runnable task) {
        System.out.printf("Reader %s requests to read\n", task);
        if (writerQueue.IsEmpty()) {
            readerQueue.putActive(task);
        } else {
            readerQueue.putBuffer(task);
            System.out.printf("Reader %s locked\n", task);
            readerQueue.acquire();
            readerQueue.putActive(readerQueue.pollBuffer());
            System.out.printf("Reader %s unlocked\n", task);
        }
        CompletableFuture.runAsync(task, executor)
                .thenAccept((value) -> {
                    readerQueue.pollActive();
                    if (readerQueue.activeIsEmpty()) {
                        handleBufferedTask();
                    }
                });
    }

    private void handleBufferedTask() {
        if (writerQueue.bufferHasItems()) {
            writerQueue.release();
            return;
        }
        if (readerQueue.bufferHasItems()) {
            readerQueue.releaseAll();
            return;
        }
        executor.shutdown();
    }

    public static ThreadPoolExecutor newFixedThreadPoolWithArrayBlockingQueue() {
        final int corePoolSize = 10;
        final int maxPoolSize = 20;
        final long keepAliveTime = 10;
        final int queueSize = 10;

        return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.MINUTES, new ArrayBlockingQueue<>(queueSize));
    }

    private static class SubscriberQueue {

        private final Semaphore semaphore = new Semaphore(0);
        private final LinkedBlockingDeque<Runnable> buffer = new LinkedBlockingDeque<>();
        private final LinkedBlockingDeque<Runnable> active = new LinkedBlockingDeque<>();

        public synchronized boolean activeIsEmpty() {
            return active.size() == 0;
        }

        public synchronized boolean activeHasItems() {
            return active.size() != 0;
        }

        public void release() {
            semaphore.release();
        }

        public void releaseAll() {
            semaphore.release(buffer.size());
        }

        public synchronized void putActive(Runnable task) {
            active.offer(task);
        }

        public synchronized Runnable pollActive() {
            return active.poll();
        }

        public synchronized boolean IsEmpty() {
            return (active.size() == 0 && buffer.size() == 0);
        }

        public boolean bufferHasItems() {
            return buffer.size() != 0;
        }

        public void acquire() {
            semaphore.acquireUninterruptibly();
        }

        public void putBuffer(Runnable task) {
            buffer.offer(task);
        }

        public Runnable pollBuffer() {
            return buffer.poll();
        }

    }

}
