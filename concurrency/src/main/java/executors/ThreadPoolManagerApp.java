package executors;

import utils.ThreadUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class ThreadPoolManagerApp {

    public static void main(String[] args) {
        final ThreadPoolManager manager = ThreadPoolManager.getInstance();
        final ExecutorService executor = Executors.newFixedThreadPool(20);
        final int N_TASKS = 20;
        final int MIN_TIMEOUT = 10;
        final int MAX_TIMEOUT = 100;

        IntStream.range(0, N_TASKS)
                .mapToObj((i) ->
                        (Runnable) () -> {
                            ThreadUtils.randomSleep(MIN_TIMEOUT, MAX_TIMEOUT);
                            if (ThreadLocalRandom.current().nextInt(5) > 1)
                                manager.submit(new ReaderTask(MIN_TIMEOUT, MAX_TIMEOUT));
                            else
                                manager.submit(new WriterTask(MIN_TIMEOUT, MAX_TIMEOUT));
                        }
                )
                .limit(N_TASKS)
                .forEach(executor::execute);

        executor.shutdown();
    }

}
