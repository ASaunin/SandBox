package concurrency;

import org.junit.Test;
import utils.ThreadUtils;

import static concurrency.LockExample.Sum;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LockExampleTest {

    @Test
    public void checkThatSumChangeLeavesUnchangedTotalAmount() {
        final SingleThreadExecutor executor1 = new SingleThreadExecutor();
        final SingleThreadExecutor executor2 = new SingleThreadExecutor();
        final Runnable task = () -> {
            final int actualValue = Sum.change();
            assertThat(actualValue, is(Sum.TOTAL_AMOUNT));
        };

        for (int i = 0; i < 100 ; i++) {
            ThreadUtils.sleep(10);
            executor1.execute(task);
            executor2.execute(task);
            Sum.print();
        }
    }

}