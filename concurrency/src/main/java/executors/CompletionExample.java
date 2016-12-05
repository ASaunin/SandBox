package executors;

import utils.ThreadUtils;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class CompletionExample {

    static class Task<T> implements Callable<T> {

        private T i;

        private Task(T i) {
            this.i = i;
        }

        @Override
        public T call() throws Exception {
            ThreadUtils.randomSleep(1, 200);
            return i;
        }

    }

    public static List<Future<Integer>> submit(ExecutorService executor, int nTasks) {
        return IntStream.range(0, nTasks)
                .mapToObj(i -> executor.submit(new Task<>(i)))
                .collect(toList());
    }

    public static List<Future<Integer>> submit(ExecutorCompletionService<Integer> executor, int nTasks) {
        return IntStream.range(0, nTasks)
                .mapToObj(i -> executor.submit(new Task<>(i)))
                .collect(toList());
    }

}
