package net.ninjacat.smooth.iterators;

import java.util.Iterator;

/**
 * Iterable wrapper around array.
 * <p>
 * Changes to underlying array will be reflected in this iterable.
 */
public class ArrayIterable<T> implements Iterable<T> {

    private final T[] data;

    /**
     * Creates a new wrapper for an array
     *
     * @param data array to wrap
     */
    public ArrayIterable(final T[] data) {
        this.data = data;
    }

    /**
     * Creates a new ArrayIterable for an array
     *
     * @param data Array to wrap in iterable
     * @param <T>  Type of array data
     * @return new instance of ArrayIterable
     */
    public static <T> ArrayIterable<T> fromArray(final T[] data) {
        return new ArrayIterable<T>(data);
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator<T>(this.data);
    }
}
