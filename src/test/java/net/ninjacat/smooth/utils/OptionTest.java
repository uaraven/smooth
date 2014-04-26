package net.ninjacat.smooth.utils;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created on 26/04/14.
 */
public class OptionTest {

    @Test(expected = IllegalStateException.class)
    public void getShouldThrowExceptionWhenQueried() throws Exception {
        Option<Object> option = Option.absent();

        option.get();
    }

    @Test
    public void getShouldReturnStoredValue() throws Exception {
        Option<String> option = Option.of("Test");

        assertThat(option.get(), is("Test"));
    }

    @Test
    public void orShouldReturnStoredValueIfPresent() throws Exception {
        Option<String> option = Option.of("Test");

        assertThat(option.or("None"), is("Test"));
    }

    @Test
    public void orShouldReturnAlternativeValueIfAbsent() throws Exception {
        Option<String> option = Option.absent();

        assertThat(option.or("None"), is("None"));
    }

    @Test
    public void orNullShouldReturnNullIfAbsent() throws Exception {
        Option<String> option = Option.absent();

        assertNull(option.orNull());
    }

    @Test
    public void orNullShouldReturnStoredValueIfPresent() throws Exception {
        Option<String> option = Option.of("Test");

        assertThat(option.orNull(), is("Test"));
    }

    @Test
    public void isPresentShouldBeTrueIfValuePresent() throws Exception {
        Option<String> option = Option.of("Test");
        assertTrue(option.isPresent());
    }


    @Test
    public void isPresentShouldBeFalseIfValueAbsent() throws Exception {
        Option<String> option = Option.absent();
        assertFalse(option.isPresent());
    }
}
