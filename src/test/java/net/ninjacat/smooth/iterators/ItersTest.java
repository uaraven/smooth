package net.ninjacat.smooth.iterators;

import net.ninjacat.smooth.collections.Collect;
import org.junit.Test;
import org.junit.internal.matchers.IsCollectionContaining;
import org.junit.matchers.JUnitMatchers;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ItersTest {

    @Test
    public void shouldGenerateRangeWithCorrectValues() throws Exception {
        Iter<Integer> range = Iters.range(10, 12);

        assertThat("Should contain values 10, 11", range, JUnitMatchers.hasItems(10, 11));
    }

    @Test
    public void shouldGenerateRangeWithCorrectSize() throws Exception {
        Iter<Integer> range = Iters.range(10, 12);

        assertThat("Should only contain two values", Collect.iteratorToList(range.iterator()).size(), is(2));
    }

    @Test
    public void shouldGenerateZeroBasedRangeWithCorrectValues() throws Exception {
        Iter<Integer> range = Iters.range(2);

        assertThat("Should contain values 0, 1", range, JUnitMatchers.hasItems(0, 1));
    }

    @Test
    public void shouldGenerateZeroBasedRangeWithCorrectSize() throws Exception {
        Iter<Integer> range = Iters.range(2);

        assertThat("Should only contain two values", Collect.iteratorToList(range.iterator()).size(), is(2));
    }


    @Test
    public void shouldGenerateRepeatedValueIterableWithCorrectValues() throws Exception {
        Iter<Integer> repeat = Iters.repeat(1, 3);
        assertThat("Should contain three values", repeat, IsCollectionContaining.hasItems(1, 1, 1));
    }

    @Test
    public void shouldGenerateRepeatedValueIterableWithCorrectSize() throws Exception {
        Iter<Integer> repeat = Iters.repeat(1, 3);
        assertThat("Should contain three values", Collect.iteratorToList(repeat.iterator()).size(), is(3));
    }
}
