package net.ninjacat.smooth.iterators;

import net.ninjacat.smooth.functions.Procedure;

import java.util.*;

/**
 * Created on 25/02/15.
 */
public final class Collectors {
    private Collectors() {
    }

    public static <T> Collector<T> arrayList() {
        return anyCollector(new ArrayList<T>());
    }

    public static <T> Collector<T> linkedList() {
        return anyCollector(new LinkedList<T>());
    }

    public static <T> Collector<T> hashSet() {
        return anyCollector(new HashSet<T>());
    }

    public static <T> Collector<T> treeSet() {
        return anyCollector(new TreeSet<T>());
    }

    private static <T> Collector<T> anyCollector(final Collection<T> collection) {
        return new Collector<T>() {
            @Override
            public Collection<T> collect(Iterable<T> iterable) {
                AddToCollection<T> collector = new AddToCollection<T>(collection);
                Iter.of(iterable.iterator()).forEach(collector);
                return collector.getCollection();
            }
        };
    }

    private static final class AddToCollection<T> extends Procedure<T> {
        private final Collection<T> collection;

        public AddToCollection(Collection<T> collection) {
            this.collection = collection;
        }

        @Override
        public void call(T t) {
            collection.add(t);
        }

        public Collection<T> getCollection() {
            return collection;
        }
    }
}
