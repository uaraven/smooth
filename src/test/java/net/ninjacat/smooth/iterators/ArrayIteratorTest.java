package net.ninjacat.smooth.iterators;

import org.junit.Test;

import java.util.Iterator;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ArrayIteratorTest {

    @Test
    public void testShouldWalkArrayWithIterator() throws Exception {
        final String[] data = {"1", "2", "3"};

        final Iterator<String> iterator = ArrayIterator.fromArray(data);

        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is("1"));
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is("2"));
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is("3"));
        assertThat(iterator.hasNext(), is(false));
    }

}