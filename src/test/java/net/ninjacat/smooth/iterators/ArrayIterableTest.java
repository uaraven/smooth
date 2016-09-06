package net.ninjacat.smooth.iterators;

import org.junit.Test;

import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;

public class ArrayIterableTest {

    @Test
    public void testShouldWrapArrayInIterable() throws Exception {
        final Integer[] data = {1, 2, 3};

        final Iterable<Integer> iterable = ArrayIterable.fromArray(data);

        assertThat(iterable, hasItems(1, 2, 3));
    }
}