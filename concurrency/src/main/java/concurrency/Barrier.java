package concurrency;

import java.util.concurrent.Semaphore;

public class Barrier {

    private final Semaphore localLock = new Semaphore(1);
    private final Semaphore globalLock = new Semaphore(0);
    private final int participants;
    private int locked;

    public Barrier(int participants) {
        this.participants = participants;
    }

    public void await() {
        localLock.acquireUninterruptibly();
        locked++;
        if (participants == locked) {
            globalLock.release(participants - 1);
            localLock.release();
            return;
        }
        localLock.release();
        globalLock.acquireUninterruptibly();
    }

}
