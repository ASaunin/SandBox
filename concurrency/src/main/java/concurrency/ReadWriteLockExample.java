package concurrency;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockExample {

    static class SynchronizedMap<K, V> {

        private Map<K, V> data = new HashMap<>();
        private ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
        private Lock readLock = lock.readLock();
        private Lock writeLock = lock.writeLock();

        public void put(K key, V value) {
            writeLock.lock();
            try {
                data.put(key, value);
            } finally {
                writeLock.unlock();
            }
        }

        public V get(K key) {
            readLock.lock();
            try {
                return data.get(key);
            } finally {
                readLock.unlock();
            }
        }

    }

}
