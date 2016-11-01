package utils;

import java.util.concurrent.ThreadLocalRandom;

public class ThreadUtils {

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void randomSleep(long minMillis, long maxMillis) {
        sleep(ThreadLocalRandom.current().nextLong(minMillis, maxMillis));
    }
}
