package executors.poolmanager;

public class Writer implements Subscriber {

    @Override
    public Runnable createTask(long minMillis, long maxMillis) {
        return new WriterTask(minMillis, maxMillis);
    }

    @Override
    public void execute(ThreadPoolManager manager, Runnable task) {
        manager.submit((WriterTask) task);
    }

}
