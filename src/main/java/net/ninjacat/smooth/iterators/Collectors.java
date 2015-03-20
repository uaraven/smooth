package net.ninjacat.smooth.iterators;

import net.ninjacat.smooth.functions.Procedure;

import java.util.*;

/**
 * Set of utility methods which return some often-used {@link Collector}s
 */
public final class Collectors {
    private Collectors() {
    }

    /**
     * Creates a collector which reads {@link Iterable} into an {@link ArrayList}
     *
     * @param <T> Type of elements.
     * @return {@link Collector} into {@link ArrayList}
     */
    public static <T> Collector<T> arrayList() {
        return anyCollector(new ArrayList<T>());
    }

    /**
     * Creates a collector which reads {@link Iterable} into an {@link LinkedList}
     *
     * @param <T> Type of elements.
     * @return {@link Collector} into {@link LinkedList}
     */
    public static <T> Collector<T> linkedList() {
        return anyCollector(new LinkedList<T>());
    }

    /**
     * Creates a collector which reads {@link Iterable} into an {@link HashSet}
     *
     * @param <T> Type of elements.
     * @return {@link Collector} into {@link HashSet}
     */
    public static <T> Collector<T> hashSet() {
        return anyCollector(new HashSet<T>());
    }

    /**
     * Creates a collector which reads {@link Iterable} into an {@link TreeSet}
     *
     * @param <T> Type of elements.
     * @return {@link Collector} into {@link TreeSet}
     */
    public static <T> Collector<T> treeSet() {
        return anyCollector(new TreeSet<T>());
    }

    private static <T> Collector<T> anyCollector(final Collection<T> collection) {
        return new Collector<T>() {
            @Override
            public Collection<T> collect(final Iterable<T> iterable) {
                final AddToCollection<T> collector = new AddToCollection<T>(collection);
                Iter.of(iterable.iterator()).forEach(collector);
                return collector.getCollection();
            }
        };
    }

    private static final class AddToCollection<T> extends Procedure<T> {
        private final Collection<T> collection;

        private AddToCollection(final Collection<T> collection) {
            this.collection = collection;
        }

        @Override
        public void call(final T t) {
            this.collection.add(t);
        }

        public Collection<T> getCollection() {
            return this.collection;
        }
    }
}
