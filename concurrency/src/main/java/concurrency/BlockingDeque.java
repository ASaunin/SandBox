package concurrency;

public interface BlockingDeque<T> {

	/**
	 * Inserts the specified element into this queue, waiting if necessary
	 * for space to become available.
	 */
	void put(T task);

	/**
	 * Retrieves and removes the last element of this deque, waiting
	 * if necessary until an element becomes available.
	 */
	T takeLast();

	/**
	 * Retrieves and removes the first element of this deque, waiting
	 * if necessary until an element becomes available.
	 */
	T takeFirst();

}
