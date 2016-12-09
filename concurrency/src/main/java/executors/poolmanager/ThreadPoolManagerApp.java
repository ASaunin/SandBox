package executors.poolmanager;

import utils.ThreadUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
                            final Subscriber subscriber = Subscriber.getRandomSubscriber(0.75);
                            final Runnable task = subscriber.createTask(MIN_TIMEOUT, MAX_TIMEOUT);
                            subscriber.execute(manager, task);
                        }
                )
                .limit(N_TASKS)
                .forEach(executor::execute);

        executor.shutdown();
    }

}
