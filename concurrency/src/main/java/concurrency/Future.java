package concurrency;

public interface Future<T> {

	/**
	 * Sets future value once. If value already set - return false.
	 */
	boolean trySet(T value);

	/**
	 * Return value if set, or wait till set.
	 */
	T get() throws InterruptedException;

}
