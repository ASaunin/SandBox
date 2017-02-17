package concurrency;

import concurrency.LoadableCache.Loader;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import utils.Ansi;
import utils.ThreadUtils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class LoadableCacheImplTest {

	private static final int THREAD_COUNT = 20;
	private static final int THREAD_SUBMIT_DELAY = 20;
	private static final int CACHE_SIZE = 3;

	private static Loader<Integer> db;
	private static LoadableCache cache;

	private static final Set<Future<Integer>> result = new HashSet<>();

	@BeforeSuite
	public void setUp() throws Exception {
		db = key -> ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
		cache = new LoadableCacheImpl<>(db);
	}

	@Test
	public void shouldWorkOnLoad() throws Exception {
		final ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
		for (int i = 0; i < THREAD_COUNT; i++) {
			result.add(executor.submit(randomReadOrCleanOperation(0.5)));
			ThreadUtils.sleep(THREAD_SUBMIT_DELAY);
		}
		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.MINUTES);

		//Test race condition absence in case of put-reset-get sequence
		result.forEach(v -> {
			Integer actual = null;
			try {
				actual = v.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			assertThat(actual)
					.as("Unexpected cached NULL result")
					.isNotNull();
		});
	}

	private static String randomKey() {
		return String.valueOf(ThreadLocalRandom.current().nextInt(0, CACHE_SIZE));
	}

	private static Callable<Integer> randomReadOrCleanOperation(double readChance) {
		if (readChance < 0 || readChance > 1)
			throw new IllegalArgumentException("Chance value is out of bounds 0 and 1");
		final double randomValue = new Random().nextDouble();
		if (randomValue < readChance)
			return new Reader();
		else
			return new RunnableAdapter<>(new Cleaner(), 0);
	}

	static private class Cleaner implements Runnable {

		@Override
		public void run() {
			final String key = randomKey();
			System.out.println(Ansi.Red.format("Cache[%s] value is invalidated", key));
			cache.reset(key);
		}

	}

	static private class Reader implements Callable<Integer> {

		@Override
		public Integer call() {
			final String key = randomKey();
			Integer result;
			try {
				System.out.println(String.format("Reading cache[%s] value from database", key));
				result = (Integer) cache.get(key);
				System.out.println(Ansi.Green.format("Cached[%s] value = %s is read successfully", key, result));
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			return result;
		}

	}

	static private class RunnableAdapter<T> implements Callable<T> {
		final Runnable task;
		final T result;

		RunnableAdapter(Runnable task, T result) {
			this.task = task;
			this.result = result;
		}

		public T call() {
			task.run();
			return result;
		}
	}

}