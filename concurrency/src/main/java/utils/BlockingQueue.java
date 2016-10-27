package utils;

import java.util.LinkedList;
import java.util.Queue;

public class BlockingQueue<T> {

    // TODO: 24.10.2016 Implement BQ with capacity & overflow notifications

    private final Queue<T> items = new LinkedList<>();

    public void put(T task) {
        synchronized (items) {
            items.offer(task);
            items.notify();
        }
    }

    public T take() {
        synchronized (items) {
            while (items.isEmpty()) {
                try {
                    items.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return items.poll();
        }
    }

}
