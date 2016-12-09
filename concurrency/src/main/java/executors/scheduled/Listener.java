package executors.scheduled;

import utils.Ansi;
import utils.ThreadUtils;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Listener implements Runnable {

    private static AtomicInteger counter = new AtomicInteger(0);
    private final int count;
    private long millis;
    private AtomicBoolean free = new AtomicBoolean(false);
    private AtomicBoolean interrupted = new AtomicBoolean(false);

    public Listener(Ticker ticker, long millis) {
        ticker.addListener(this);
        this.millis = millis;
        this.count = counter.incrementAndGet();
    }

    public void submit() {
        free.compareAndSet(false, true);
    }

    @Override
    public void run() {
        final Thread currentThread = Thread.currentThread();
        currentThread.setName("Listener" + count);
        while (!interrupted.get()) {
            if (free.get()) {
                System.out.println(Ansi.Yellow.colorize(currentThread.getName() + " was submitted"));
                ThreadUtils.sleep(millis);
                System.out.println(Ansi.Green.colorize(currentThread.getName() + " finished task"));
                free.set(false);
            }
        }
    }

    public void interrupt() {
        interrupted.set(true);
    }

}
