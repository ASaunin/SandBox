package executors.scheduled;

import utils.ThreadUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Ticker {

    private static final int N_THREADS = 10;
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private final List<Listener> listeners = new ArrayList<>();

    public Ticker(long period, TimeUnit unit) {
        final Runnable task = () -> listeners.forEach(Listener::submit);
        executor.scheduleAtFixedRate(task, 0, period, unit);
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    public static void main(String[] args) {
        final Ticker ticker = new Ticker(1000, TimeUnit.MILLISECONDS);
        final ExecutorService executor = Executors.newFixedThreadPool(N_THREADS);;

        final Random generator = new Random();
        final List<Listener> listeners = Stream.generate(() -> new Listener(ticker, generator.nextInt(2000)))
                .limit(10).collect(toList());

        listeners.forEach(executor::execute);

        ThreadUtils.sleep(10000);

        listeners.forEach(Listener::interrupt);
        executor.shutdown();
    }

}
