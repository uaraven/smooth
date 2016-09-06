package net.ninjacat.smooth.iterators;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class CollectorsTest {


    private Iter<Integer> iter;

    @Before
    public void setUp() throws Exception {
        this.iter = Iter.of(1, 2, 3);
    }

    @Test
    public void shouldCollectElementsToArrayList() throws Exception {
        Collection<Integer> collection = this.iter.collectWith(Collectors.<Integer>arrayList());

        assertThat("collected data should contain elements from iterable", collection, hasItems(1, 2, 3));
        assertThat("collection type should be ArrayList", collection, instanceOf(ArrayList.class));
    }

    @Test
    public void shouldCollectElementsToLinkedList() throws Exception {
        Collection<Integer> collection = this.iter.collectWith(Collectors.<Integer>linkedList());

        assertThat("collected data should contain elements from iterable", collection, hasItems(1, 2, 3));
        assertThat("collection type should be ArrayList", collection, instanceOf(LinkedList.class));
    }

    @Test
    public void shouldCollectElementsToHashSet() throws Exception {
        Collection<Integer> collection = this.iter.collectWith(Collectors.<Integer>hashSet());

        assertThat("collected data should contain elements from iterable", collection, hasItems(1, 2, 3));
        assertThat("collection type should be ArrayList", collection, instanceOf(HashSet.class));
    }

    @Test
    public void shouldCollectElementsToTreeSet() throws Exception {
        Collection<Integer> collection = this.iter.collectWith(Collectors.<Integer>treeSet());

        assertThat("collected data should contain elements from iterable", collection, hasItems(1, 2, 3));
        assertThat("collection type should be ArrayList", collection, instanceOf(TreeSet.class));
    }
}
