package concurrency;

import java.util.Deque;
import java.util.LinkedList;

public class SimpleBlockingDeque<T> implements BlockingDeque<T> {

    private final Deque<T> items = new LinkedList<>();

    public void put(T task) {
        synchronized (items) {
            items.offer(task);
            items.notify();
        }
    }

    public T takeFirst() {
        synchronized (items) {
            while (items.isEmpty()) {
                try {
                    items.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return items.pollFirst();
        }
    }

    public T takeLast() {
        synchronized (items) {
            while (items.isEmpty()) {
                try {
                    items.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return items.pollLast();
        }
    }

}
