package executors.poolmanager;

public class Reader implements Subscriber {

    @Override
    public Runnable createTask(long minMillis, long maxMillis) {
        return new ReaderTask(minMillis, maxMillis);
    }

    @Override
    public void execute(ThreadPoolManager manager, Runnable task) {
        manager.submit((ReaderTask) task);
    }

}
