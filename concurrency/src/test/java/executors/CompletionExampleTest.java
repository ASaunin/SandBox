package executors;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class CompletionExampleTest {

    private static final int N_THREADS = 2;
    private static final int N_TASKS = 20;
    private static ExecutorService executor;

    @BeforeClass
    public static void init() {
        executor = Executors.newFixedThreadPool(N_THREADS);
    }

    @Test
    public void submitOrderedResult() throws Exception {
        final List<Future<Integer>> results = CompletionExample.submit(executor, N_TASKS);

        for (int expected = 0; expected < N_TASKS; expected++) {
            final Future<Integer> future = results.get(expected);
            final int actual = future.get();
            System.out.printf("Ordered: %d\n", actual);
            assertThat(actual, is(expected));
        }
    }

    @Test
    public void submitUnorderedResult() throws Exception {
        final ExecutorCompletionService<Integer> executorCompletionService = new ExecutorCompletionService<>(executor);
        final List<Future<Integer>> results = CompletionExample.submit(executorCompletionService, N_TASKS);

        for (int expected = 0; expected < N_TASKS; expected++) {
            final Future<Integer> future = executorCompletionService.take();
            final int actual = future.get();
            System.out.printf("Udrdered: %d\n", actual);
            assertThat(results, hasItem(future));
        }
    }

    @AfterClass
    public static void finalise() {
        executor.shutdownNow();
    }

}