package net.ninjacat.smooth.iterators;

import java.util.Collection;

/**
 * Created on 25/02/15.
 */
public interface Collector<T> {
    Collection<T> collect(Iterable<T> iterable);
}
