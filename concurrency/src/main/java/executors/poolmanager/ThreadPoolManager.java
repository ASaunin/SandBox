package executors.poolmanager;

import utils.Ansi;

import java.util.concurrent.*;

/**
 * Custom tread pool manager provides concurrent single thread priority writing and multithreaded reading
 */
// TODO: 10.12.2016 Rewrite code using one input queue for both task types
public class ThreadPoolManager {

    private static final ThreadPoolManager INSTANCE = new ThreadPoolManager();
    private static final ExecutorService executor = newFixedThreadPoolWithArrayBlockingQueue();
    private static final SubscriberQueue readerQueue = new SubscriberQueue();
    private static final SubscriberQueue writerQueue = new SubscriberQueue();

    public static ThreadPoolManager getInstance() {
        return INSTANCE;
    }

    public void submit(WriterTask task) {
        System.out.printf("%s requested an access\n", task);
        if (readerQueue.activeIsEmpty() && writerQueue.activeIsEmpty()) {
            writerQueue.putActive(task);
        } else {
            writerQueue.waitUntilSubmit(task);
        }
        CompletableFuture.runAsync(task, executor)
                .thenAccept((value) -> {
                    writerQueue.pollActive();
                    handleBufferedTask();
                });
    }

    public void submit(ReaderTask task) {
        System.out.printf("%s requested an access\n", task);
        if (writerQueue.IsEmpty()) {
            readerQueue.putActive(task);
        } else {
            readerQueue.waitUntilSubmit(task);
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

    /**
     * Only active buffer should have synchronized methods
     */
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

        public boolean bufferIsEmpty() {
            return buffer.size() == 0;
        }

        public boolean bufferHasItems() {
            return buffer.size() != 0;
        }

        public synchronized boolean IsEmpty() {
            return (active.size() == 0 && buffer.size() == 0);
        }

        public synchronized void putActive(Runnable task) {
            active.offer(task);
        }

        public synchronized void putActiveFromBuffer() {
            active.offer(pollBuffer());
        }

        public void waitUntilSubmit(Runnable task) {
            putBuffer(task);
            System.out.println(Ansi.Red.format("%s locked", task));
            acquire();
            putActiveFromBuffer();
            System.out.printf("%s unlocked\n", task);
        }

        public void putBuffer(Runnable task) {
            buffer.offer(task);
        }

        public Runnable pollBuffer() {
            return buffer.poll();
        }

        public synchronized Runnable pollActive() {
            return active.poll();
        }

        public void acquire() {
            semaphore.acquireUninterruptibly();
        }

        public void release() {
            semaphore.release();
        }

        public void releaseAll() {
            semaphore.release(buffer.size());
        }
    }

}
