package concurrency;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LoadableCacheImpl<T> implements LoadableCache<T> {

	private final ConcurrentMap<String, T> cache = new ConcurrentHashMap<>();
	private final Loader<T> db;

	public LoadableCacheImpl(Loader<T> db) {
		this.db = db;
	}

	@Override
	public T get(String key) throws InterruptedException {
		if (!cache.containsKey(key)) {
			final T value = db.load(key);
			cache.put(key, value);
			return value; //Race condition is possible in case of no return value (simple solution)
		}
		return cache.get(key);
	}

	@Override
	public void reset(String key) {
		cache.remove(key);
	}

}
