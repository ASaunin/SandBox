package concurrency;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.model.TestTimedOutException;

import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BoundedBlockingQueueTest {

    private static final int MAX_CAPACITY = 2;
    private BoundedBlockingQueue<Integer> queue = new BoundedBlockingQueue<>(MAX_CAPACITY);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test(timeout = 100)
    public void takingItemFromEmptyQueueFailsByTimeout() throws Exception {
        thrown.expect(TestTimedOutException.class);
        queue.take();
    }

    @Test(timeout = 100)
    public void puttingItemToFullQueueFailsByTimeout() throws Exception {
        for (int i = 0; i < MAX_CAPACITY; i++) {
            queue.put(i);
        }
        thrown.expect(TestTimedOutException.class);
        queue.put(MAX_CAPACITY);
    }

    @Test
    public void putAndTakenItemsAreEqual() throws Exception {
        final int expectedValue = ThreadLocalRandom.current().nextInt();
        queue.put(expectedValue);

        assertThat(queue.take(), is(expectedValue));
    }

}