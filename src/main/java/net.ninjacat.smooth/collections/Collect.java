package net.ninjacat.smooth.collections;

import java.util.*;

/**
 * User: raven
 * Date: 17/06/13
 */
public class Collect {

    public static <E> List<E> listOf(E... elements) {
        return Arrays.asList(elements);
    }

    public static <E> Set<E> setOf(E... elements) {
        Set<E> result = new HashSet<E>();
        Collections.addAll(result, elements);
        return result;
    }

    public static <E> List<E> iteratorToList(Iterator<E> iter) {
        return Collections.unmodifiableList((List<? extends E>) iteratorToCollection(iter, new ArrayList<E>()));
    }

    public static <E> Set<E> iteratorToSet(Iterator<E> iter) {
        return Collections.unmodifiableSet((Set<? extends E>) iteratorToCollection(iter, new HashSet<E>()));
    }

    private static <E> Collection<E> iteratorToCollection(Iterator<E> iter, Collection<E> collection) {
        while(iter.hasNext()) {
            collection.add(iter.next());
        }
        return collection;
    }

    private Collect() {
    }
}
