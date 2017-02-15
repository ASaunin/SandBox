package properties;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SingletonPropertyHandlerTest {

    private final static SingletonPropertyHandler props = SingletonPropertyHandler.getInstance();

    @Test
    public void wrongKeyReturnsDefaultValue() throws Exception {
        assertThat(props.getValue("wrong.key")).isEqualTo("Here should be a description soon");
    }

    @Test
    public void existingKeyReturnsRightValue() throws Exception {
        assertThat(props.getValue("right.key")).isEqualTo("Lorem ipsum");
    }

}