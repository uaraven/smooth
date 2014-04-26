package net.ninjacat.collections;

import net.ninjacat.smooth.iterators.MultiIterable;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;

import static net.ninjacat.collections.IterFixtures.verifyNext;
import static net.ninjacat.collections.IterFixtures.verifyNoNext;

/**
 * User: raven
 * Date: 06/09/13
 */
public class MultiIterableTest {

    @Test
    public void shouldWalkSimpleCollection() throws Exception {
        MultiIterable<String> mi = new MultiIterable<String>();
        mi.append(Arrays.asList("a", "b", "c"));

        Iterator<String> iter = mi.iterator();
        verifyNext(iter, "a");
        verifyNext(iter, "b");
        verifyNext(iter, "c");
        verifyNoNext(iter);
    }

    @Test
    public void shouldWalkCombinedCollection() throws Exception {
        MultiIterable<String> mi = new MultiIterable<String>();
        mi.append(Arrays.asList("a", "b"));
        mi.append(Arrays.asList("c"));

        Iterator<String> iter = mi.iterator();
        verifyNext(iter, "a");
        verifyNext(iter, "b");
        verifyNext(iter, "c");
        verifyNoNext(iter);
    }

}
