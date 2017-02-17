package executors.poolmanager;

import java.util.Random;

public interface Subscriber {

    enum type {READER, WRITER}

    Runnable createTask(long minMillis, long maxMillis);

    void execute(ThreadPoolManager manager, Runnable task);

    static Subscriber getRandomSubscriber(double chance) {
        if (chance < 0 || chance > 1)
            throw new IllegalArgumentException("Chance value is out of bounds 0 and 1");

        final double randomValue = new Random().nextDouble();
        if (randomValue < chance)
            return new Reader();
        else
            return new Writer();
    }

}
