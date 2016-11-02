package concurrency;

import java.util.LinkedList;
import java.util.Queue;

public class Worker {

    private final Queue<Runnable> tasks = new LinkedList<>();

    private class Runner implements Runnable {

        @Override
        public void run() {
            Runnable task;
            while (true) {
                synchronized (tasks) {
                    while (tasks.isEmpty()) {
                        try {
                            tasks.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    task = tasks.poll();
                }
                task.run();
            }
        }

    }

    public Worker() {
        new Thread(new Runner()).start();
    }

    public void execute(Runnable task) {
        synchronized (tasks) {
            tasks.offer(task);
            tasks.notify();
        }
    }

    public static void main(String[] args) {
        final Worker worker = new Worker();
        for (int i = 0; i < 10; i++) {
            final int finalI = i;
            final Runnable task = () -> System.out.printf("Thread %d execute\n", finalI);
            worker.execute(task);
        }
    }

}
