package concurrency;

import utils.BlockingQueue;

public class SingleThreadExecutor {

    private BlockingQueue<Runnable> queue = new BlockingQueue<>();

    private class Runner implements Runnable {
        @Override
        public void run() {
            while (true) {
                queue.take().run();
            }
        }
    }

    public SingleThreadExecutor() {
        new Thread(new Runner()).start();
    }

    public void execute(Runnable task) {
        queue.put(task);
    }

    public static void main(String[] args) {
        final SingleThreadExecutor executor = new SingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            final int finalI = i;
            final Runnable task = () -> System.out.printf("Thread %d execute\n", finalI);
            executor.execute(task);
        }
    }

}
