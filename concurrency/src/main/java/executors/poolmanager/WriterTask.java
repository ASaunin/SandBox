package executors.poolmanager;

import utils.Ansi;
import utils.ThreadUtils;

import java.util.concurrent.atomic.AtomicInteger;

class WriterTask implements Runnable {

    private static AtomicInteger counter = new AtomicInteger(0);
    private final int count;
    private long minMillis;
    private long maxMillis;

    WriterTask(long minMillis, long maxMillis) {
        this.minMillis = minMillis;
        this.maxMillis = maxMillis;
        this.count = counter.incrementAndGet();
    }

    @Override
    public void run() {
        final Thread currentThread = Thread.currentThread();
        currentThread.setName("Writer" + count);
        System.out.println(Ansi.Yellow.format("%s started work", currentThread.getName()));
        ThreadUtils.randomSleep(minMillis, maxMillis);
        System.out.printf(Ansi.Green.format("%s finished work\n", currentThread.getName()));
    }

    @Override
    public String toString() {
        return "Writer" + count;
    }

}
