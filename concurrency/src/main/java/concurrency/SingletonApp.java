package concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class SingletonApp {

    public static void main(String[] args) {
        final ExecutorService executorService = Executors.newCachedThreadPool();

        System.out.println("Start");
        IntStream.range(0, 10)
                .mapToObj(i -> Singleton.getInstance())
                .forEach(System.out::println);
        System.out.println("Finish");
    }

}
