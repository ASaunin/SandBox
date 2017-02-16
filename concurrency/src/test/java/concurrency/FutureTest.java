package concurrency;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.internal.thread.ThreadTimeoutException;

import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class FutureTest {

	private static final int expected = ThreadLocalRandom.current().nextInt();

	@DataProvider
	public Object[][] future() {
		return new Object[][]{{new SimpleFutureValue<>()}, {new FutureValue<>()}};
	}

	@Test(priority = 0, dataProvider = "future", timeOut = 100, expectedExceptions = ThreadTimeoutException.class)
	public void getAttemptBeforeSetShouldFailByTimeout(Future future) throws Exception {
		future.get();
	}

	@SuppressWarnings("unchecked")
	@Test(priority = 1, dataProvider = "future")
	public void getAttemptAfterSetShouldAppropriateValue(Future future) throws Exception {
		future.trySet(expected);
		assertThat(future.get()).isEqualTo(expected);
	}

}
