package concurrency;

import javafx.util.Pair;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SingletonTest {

    @Test
    public void checkGetInstanceReturnsSameObject() {
        System.out.println("Start");
        IntStream.range(0, 10)
                .mapToObj(i -> new Pair<>(Singleton.getInstance(), Singleton.getInstance()))
                .forEach(p -> assertThat(p.getKey(), is(p.getValue())));
        System.out.println("Finish");
    }

}