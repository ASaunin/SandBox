package model;

import concurrency.Barrier;
import lombok.Getter;
import utils.ThreadUtils;

import static model.Sprinter.State.*;

public class Sprinter<T> implements Runnable {

    public enum State {
        STARTED, FINISHED, PASSED
    }

    @Getter
    private State state;

    private Barrier barrier;

    @Override
    public void run() {
        state = STARTED;
        System.out.println(this + " started");
        ThreadUtils.randomSleep(100, 500);

        state = FINISHED;
        System.out.println(this + " finished");

        barrier.await();
        state = PASSED;
        System.out.println(this + " passed");
    }

    public Sprinter(Barrier barrier) {
        this.barrier = barrier;
    }
}
