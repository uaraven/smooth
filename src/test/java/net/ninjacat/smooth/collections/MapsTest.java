package net.ninjacat.smooth.collections;

import net.ninjacat.smooth.functions.Func;
import net.ninjacat.smooth.utils.Pair;
import org.junit.Test;

import java.util.List;
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

    @Test
    public void shouldCreateMapFromIterableWithKeyGenerator() throws Exception {
        final List<String> list = Lists.of("1", "2");

        final Map<Integer, String> map = Maps.toMap(list, new Func<Integer, String>() {
            @Override
            public Integer apply(final String s) {
                return Integer.parseInt(s);
            }
        });

        assertThat(map.get(1), is("1"));
        assertThat(map.get(2), is("2"));
        assertThat(map.size(), is(2));

    }
}