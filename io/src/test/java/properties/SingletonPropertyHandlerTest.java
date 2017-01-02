package properties;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class SingletonPropertyHandlerTest {

    private final static SingletonPropertyHandler props = SingletonPropertyHandler.getInstance();

    @Test
    public void wrongKeyReturnsDefaultValue() throws Exception {
        assertThat(props.getValue("wrong.key"), is("Here should be a description soon"));
    }

    @Test
    public void existingKeyReturnsRightValue() throws Exception {
        assertThat(props.getValue("right.key"), is("Lorem ipsum"));
    }

}