package net.ninjacat.smooth.iterators;

import java.util.Collection;

/**
 * Allows to collect values from {@link Iterable} to a {@link Collection}.
 * {@link net.ninjacat.smooth.iterators.Collectors} has implementations of collectors for some collection types.
 */
public interface Collector<T> {
    /**
     * Collects elements from {@link Iterable} into a {@link Collection}.
     *
     * @param iterable Iterable to collect elements from.
     * @return New collection.
     */
    Collection<T> collect(Iterable<T> iterable);
}
