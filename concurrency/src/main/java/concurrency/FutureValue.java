package concurrency;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class FutureValue<T> implements Future<T> {

	private final ReentrantLock lock = new ReentrantLock();
	private final Condition notEmpty = lock.newCondition();
	private T value = null;

	@Override
	public boolean trySet(T v) {
		if (lock.tryLock()) {
			try {
				value = v;
				notEmpty.signal();
				return true;
			} finally {
				lock.unlock();
			}
		}
		return false;
	}

	@Override
	public T get() throws InterruptedException {
		lock.lock();
		try {
			if (null==value) {
				notEmpty.awaitUninterruptibly();
			}
			return value;
		} finally {
			lock.unlock();
		}
	}

}
