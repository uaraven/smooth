package net.ninjacat.smooth.collections;

import net.ninjacat.smooth.utils.Pair;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MapsTest {

    @Test
    public void shouldCreateAMapFromPairs() throws Exception {
        final Map<Integer, String> map = Maps.of(Pair.of(1, "first"), Pair.of(2, "second"));

        assertThat(map.get(1), is("first"));
        assertThat(map.get(2), is("second"));
        assertThat(map.size(), is(2));
    }

    @Test
    public void shouldCreateAMapFromList() throws Exception {
        final Map<Integer, String> map = Maps.of(1, "first", 2, "second");

        assertThat(map.get(1), is("first"));
        assertThat(map.get(2), is("second"));
        assertThat(map.size(), is(2));
    }
}