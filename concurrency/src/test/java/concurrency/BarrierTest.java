package concurrency;

import model.Sprinter;
import model.Sprinter.State;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static model.Sprinter.State.*;
import static org.assertj.core.api.Assertions.assertThat;
import static utils.ThreadUtils.sleep;

public class BarrierTest {

    private static final int NUMBER_OF_SPRINTERS = 5;
    private static ExecutorService EXECUTOR;

    @BeforeClass
    public static void setUp() throws Exception {
        EXECUTOR = Executors.newFixedThreadPool(NUMBER_OF_SPRINTERS);
    }

    @Test
    public void checkThatSprintersGetFinishSimultaneously() throws Exception {
        final Barrier barrier = new Barrier(NUMBER_OF_SPRINTERS);

        final List<Sprinter> sprinters = IntStream.range(0, NUMBER_OF_SPRINTERS)
                .mapToObj((i) -> new Sprinter<State>(barrier))
                .collect(toList());

        sprinters.forEach(EXECUTOR::execute);
        sleep(10);
        sprinters.forEach(s -> assertThat(s.getState()).isEqualTo(STARTED));

        sleep(250);
        sprinters.forEach(s -> assertThat(s.getState()).isIn(STARTED, FINISHED));

        sleep(250);
        sprinters.forEach(s -> assertThat(s.getState()).isEqualTo(PASSED));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        EXECUTOR.shutdown();
    }

}
