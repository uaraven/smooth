package net.ninjacat.smooth.iterators;

import net.ninjacat.smooth.collections.Collect;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ItersTest {

    @Test
    public void shouldGenerateRangeWithCorrectValues() throws Exception {
        final Iter<Integer> range = Iters.range(10, 12);

        assertThat("Should contain values 10, 11", range, hasItems(10, 11));
    }

    @Test
    public void shouldGenerateRangeWithCorrectSize() throws Exception {
        final Iter<Integer> range = Iters.range(10, 12);

        assertThat("Should only contain two values", Collect.iteratorToList(range.iterator()).size(), is(2));
    }

    @Test
    public void shouldGenerateZeroBasedRangeWithCorrectValues() throws Exception {
        final Iter<Integer> range = Iters.range(2);

        assertThat("Should contain values 0, 1", range, JUnitMatchers.hasItems(0, 1));
    }

    @Test
    public void shouldGenerateZeroBasedRangeWithCorrectSize() throws Exception {
        final Iter<Integer> range = Iters.range(2);

        assertThat("Should only contain two values", Collect.iteratorToList(range.iterator()).size(), is(2));
    }


    @Test
    public void shouldGenerateRepeatedValueIterableWithCorrectValues() throws Exception {
        final Iter<Integer> repeat = Iters.repeat(1, 3);
        assertThat("Should contain three values", repeat, IsCollectionContaining.hasItems(1, 1, 1));
    }

    @Test
    public void shouldGenerateRepeatedValueIterableWithCorrectSize() throws Exception {
        final Iter<Integer> repeat = Iters.repeat(1, 3);
        assertThat("Should contain three values", Collect.iteratorToList(repeat.iterator()).size(), is(3));
    }
}
