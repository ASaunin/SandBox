package concurrency;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@Test(suiteName = "bounded-blocking-deque")
public class BoundedBlockingDequeConcurrentTest {

    private static final int MAX_CAPACITY = 100;
    private static final BoundedBlockingDeque<Integer> queue = new BoundedBlockingDeque<>(MAX_CAPACITY);
    private static final int startValue = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE - MAX_CAPACITY);
    private static AtomicInteger firstExpected = new AtomicInteger(startValue);
    private static AtomicInteger lastExpected = new AtomicInteger(startValue + MAX_CAPACITY - 1);

    @BeforeTest
    public void putValues() {
        for (int i = 0; i < MAX_CAPACITY; i++) {
            queue.put(startValue + i);
        }
    }

    @Test(testName = "take-first", threadPoolSize = MAX_CAPACITY, invocationCount = MAX_CAPACITY)
    public void takeFirstShouldReturnAscendingOrder() throws Exception {
        final int actual = queue.takeFirst();
        final int expected = firstExpected.getAndIncrement();
        assertThat(actual).isEqualTo(expected);
    }

    @Test(testName = "take-last", threadPoolSize = MAX_CAPACITY, invocationCount = MAX_CAPACITY)
    public void takeFirstShouldReturnDescendingOrder() throws Exception {
        final int actual = queue.takeLast();
        final int expected = lastExpected.getAndDecrement();
        assertThat(actual).isEqualTo(expected);
    }

}