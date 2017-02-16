package concurrency;

public class SimpleFutureValue<T> implements Future<T> {

	private volatile boolean set;
	private T value;

	@Override
	public boolean trySet(T v) {
		if (set) {
			return false;
		} else {
			synchronized(this) {
				if (set) {
					return false;
				} else {
					value = v;
					set = true;
					notifyAll();
					return true;
				}
			}
		}
	}

	@Override
	public T get() throws InterruptedException {
		if (!set) {
			synchronized(this) {
				while (!set) {
					wait();
				}
			}
		}
		return value;
	}

}
