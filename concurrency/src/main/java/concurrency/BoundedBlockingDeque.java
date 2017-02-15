package concurrency;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedBlockingDeque<T> implements BlockingDeque<T> {

    private final Deque<T> items = new LinkedList<>();
    private final int capacity;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();

    public BoundedBlockingDeque(int capacity) {
        this.capacity = capacity;
    }

    @Override
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

    @Override
    public T takeFirst() {
        lock.lock();
        try {
            if (items.isEmpty()) {
                notEmpty.awaitUninterruptibly();
            }
            notFull.signal();
            return items.pollFirst();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public T takeLast() {
        lock.lock();
        try {
            if (items.isEmpty()) {
                notEmpty.awaitUninterruptibly();
            }
            notFull.signal();
            return items.pollLast();
        } finally {
            lock.unlock();
        }
    }

}
