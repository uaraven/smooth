package net.ninjacat.smooth.iterators;

import java.util.Iterator;

/**
 * Array iterator.
 * <p/>
 * Reading from this iterator will reflect changes in the underlying array.
 */
public class ArrayIterator<T> implements Iterator<T> {

    private final T[] data;
    private int position;

    /**
     * Creates a new wrapper for an array
     *
     * @param data array to wrap
     */
    public ArrayIterator(final T[] data) {
        this.data = data;
        this.position = 0;
    }

    /**
     * Creates a new ArrayIterator for an array
     *
     * @param data Array to wrap in iterator
     * @param <T>  Type of array data
     * @return new instance of ArrayIterator
     */
    public static <T> ArrayIterator<T> fromArray(final T[] data) {
        return new ArrayIterator<T>(data);
    }

    public boolean hasNext() {
        return this.position < this.data.length;
    }

    public synchronized T next() {
        return this.data[this.position++];
    }

    public void remove() {
        throw new UnsupportedOperationException(getClass().getCanonicalName() + " does not support remove()");
    }
}
