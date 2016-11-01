package concurrency;

import org.junit.Test;
import utils.ThreadUtils;

import static concurrency.ReentrantLockExample.Sum;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ReentrantLockExampleTest {

    @Test
    public void checkThatSumChangeLeavesUnchangedTotalAmount() {
        final SingleThreadExecutor executor1 = new SingleThreadExecutor();
        final SingleThreadExecutor executor2 = new SingleThreadExecutor();
        final Runnable task = () -> {
            final int actualValue = Sum.change();
            assertThat(actualValue, is(Sum.TOTAL_AMOUNT));
        };

        while (true) {
            ThreadUtils.sleep(10);
            executor1.execute(task);
            executor2.execute(task);
            Sum.print();
        }
    }

}