package net.ninjacat.smooth.collections;

import java.util.*;

/**
 * Utility functions for collections
 */
public final class Collect {

    private Collect() {
    }

    /**
     * Create list of elements
     *
     * @param elements elements of the future list
     * @param <E>      type of the elements
     * @return Unmodifiable list
     */
    public static <E> List<E> listOf(E... elements) {
        return Collections.unmodifiableList(Arrays.asList(elements));
    }

    /**
     * Create a set of elements
     *
     * @param elements elements of the future set
     * @param <E>      type of the elements
     * @return Unmodifiable set
     */
    public static <E> Set<E> setOf(E... elements) {
        Set<E> result = new HashSet<E>();
        Collections.addAll(result, elements);
        return Collections.unmodifiableSet(result);
    }

    /**
     * Converts {@link java.util.Iterator} to list
     *
     * @param iter iterator to read into list
     * @param <E>  type of elements
     * @return Unmodifiable list of elements from iterator
     */
    public static <E> List<E> iteratorToList(Iterator<E> iter) {
        return Collections.unmodifiableList((List<? extends E>) iteratorToCollection(iter, new ArrayList<E>()));
    }

    /**
     * Converts {@link java.util.Iterator} to a set
     *
     * @param iter iterator to read into a set
     * @param <E>  type of elements
     * @return Unmodifiable set of elements from iterator
     */
    public static <E> Set<E> iteratorToSet(Iterator<E> iter) {
        return Collections.unmodifiableSet((Set<? extends E>) iteratorToCollection(iter, new HashSet<E>()));
    }

    /**
     * <p>
     * Reads elements from {@link java.util.Iterator} to a supplied collection
     * </p>
     * <p>Elements read from iterator will be added to existing elements in the collection</p>
     *
     * @param iter       iterator to read into collection
     * @param collection supplied collection
     * @param <E>        type of elements
     * @return Same collection as passed to the function
     */
    private static <E> Collection<E> iteratorToCollection(Iterator<E> iter, Collection<E> collection) {
        while (iter.hasNext()) {
            collection.add(iter.next());
        }
        return collection;
    }
}
