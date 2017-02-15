package concurrency;


import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.internal.thread.ThreadTimeoutException;

import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;

public class BoundedBlockingDequeTest {

    private static final int MAX_CAPACITY = 3;
    private static BoundedBlockingDeque<Integer> queue;

    @BeforeMethod
    public void putValues() {
        queue = new BoundedBlockingDeque<>(MAX_CAPACITY);
    }


    @Test(timeOut = 100, expectedExceptions = ThreadTimeoutException.class)
    public void takingItemFromEmptyQueueFailByTimeout() throws Exception {
        queue.takeFirst();
    }

    @Test(timeOut = 100, expectedExceptions = ThreadTimeoutException.class)
    public void puttingItemToFullQueueFailByTimeout() throws Exception {
        for (int i = 0; i < MAX_CAPACITY; i++) {
            queue.put(i);
        }
        queue.put(MAX_CAPACITY);
    }

    @Test
    public void putAndTakenFirstItemsAreEqual() throws Exception {
        final int expectedValue = ThreadLocalRandom.current().nextInt();
        queue.put(expectedValue);
        queue.put(expectedValue + 1);
        queue.put(expectedValue + 2);

        assertThat(queue.takeFirst()).isEqualTo(expectedValue);
        assertThat(queue.takeFirst()).isEqualTo(expectedValue + 1);
        assertThat(queue.takeFirst()).isEqualTo(expectedValue + 2);
    }

    @Test
    public void putAndTakenLastItemsAreEqualInReverseOrder() throws Exception {
        final int expectedValue = ThreadLocalRandom.current().nextInt();
        queue.put(expectedValue);
        queue.put(expectedValue + 1);
        queue.put(expectedValue + 2);

        assertThat(queue.takeLast()).isEqualTo(expectedValue + 2);
        assertThat(queue.takeLast()).isEqualTo(expectedValue + 1);
        assertThat(queue.takeLast()).isEqualTo(expectedValue);
    }

}