package utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedBlockingQueue<T> {

    private final Queue<T> items = new LinkedList<>();
    private final int capacity;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();

    public BoundedBlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    public void put(T task) {
        lock.lock();
        try {
            if (items.size() == capacity) {
                notFull.awaitUninterruptibly();
            }
            items.offer(task);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public T take() {
        lock.lock();
        try {
            if (items.isEmpty()) {
                notEmpty.awaitUninterruptibly();
            }
            notFull.signal();
            return items.poll();
        } finally {
            lock.unlock();
        }
    }

}
