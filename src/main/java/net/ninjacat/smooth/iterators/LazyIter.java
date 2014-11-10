package net.ninjacat.smooth.iterators;

import net.ninjacat.smooth.collections.Collect;
import net.ninjacat.smooth.functions.Func;
import net.ninjacat.smooth.functions.Function2;
import net.ninjacat.smooth.functions.Predicate;
import net.ninjacat.smooth.functions.Promise;

import java.util.*;

/**
 * <p>Functional style <strong>lazy</strong> immutable rich iterator</p>
 * <p/>
 * <p>Main difference from standard {@link java.util.Iterator} is that this iterator allows functions to be applied to the elements.
 * It supports standard operations like {@link #filter(net.ninjacat.smooth.functions.Predicate)}, {@link #map(net.ninjacat.smooth.functions.Func)}
 * or {@link #reduce(Object, net.ninjacat.smooth.functions.Function2)} and others.
 * </p>
 * <p>
 * Most of the methods are lazy, they do not perform any action at the call time, but return either {@link Iterable} or {@link Promise}.
 * </p>
 * <p/>
 * <p>This is essentially a rich wrapper around {@link Iterator} over collection. Standard limitations of iterators apply,
 * like restriction of changing collection during iteration</p>
 *
 * @param <E> Type of the elements in the iterator
 */
public class LazyIter<E> implements Iterable<E> {
    private final Iter<E> collection;

    private LazyIter(Iterable<E> c) {
        collection = new Iter<E>(c.iterator());
    }

    private LazyIter(Iterator<E> c) {
        collection = new Iter<E>(c);
    }

    /**
     * Constructs new lazy iterable from collection. Will create internal copy, so original collection may be changed
     *
     * @param coll collection
     * @param <E>  type of collection elements
     * @return new lazy iterable
     */
    public static <E> LazyIter<E> of(Collection<E> coll) {
        return new LazyIter<E>(Collections.unmodifiableCollection(coll));
    }

    public static <E> LazyIter<E> of(E head, Collection<E> tail) {
        List<E> holder = new ArrayList<E>(tail.size() + 1);
        holder.add(head);
        holder.addAll(tail);
        return new LazyIter<E>(holder);
    }

    /**
     * Create new lazy iterable from array, sequence of parameters.
     *
     * @param data array or varargs
     * @param <E>  type of elements
     * @return new lazy iterable
     */
    public static <E> LazyIter<E> of(E... data) {
        return of(Arrays.asList(data));
    }

    /**
     * This method is <strong>not lazy</strong>
     *
     * @return {@link List} containing all the items from this collection. Returned list is immutable
     */
    public List<E> toList() {
        return Collect.iteratorToList(collection.iterator());
    }

    /**
     * This method is <strong>not lazy</strong>
     *
     * @return {@link Set} containing all the items from this collection. Returned list is immutable
     */
    public Set<E> toSet() {
        return Collect.iteratorToSet(collection.iterator());
    }

    /**
     * <p>
     * Returns an array containing all elements contained in this Iterable. If the specified array is large enough to
     * hold the elements, the specified array is used, otherwise an array of the same type is created.
     * If the specified array is used and is larger than this List, the array element following the
     * collection elements is set to null.
     * </p>
     *
     * @param array the array
     * @return array of the elements from this iterable.
     * @throws ArrayStoreException if the type of an element in this List cannot be stored in the type of the specified array.
     */
    public E[] toArray(E[] array) {
        return toList().toArray(array);
    }

    /**
     * Returns {@link java.util.Iterator} for the elements in this Iterable
     *
     * @return An Iterator instance.
     */
    public Iterator<E> iterator() {
        return collection.iterator();
    }

    /**
     * <p>Maps all the values in the collection to other values using supplied function.</p>
     * <p>This function will not create new collection, instead transformation function will be applied to
     * the next original collection's element during each call to {@link java.util.Iterator#next()} </p>
     *
     * @param func mapping function
     * @param <R>  result type
     * @return Iterable collection of mapped values
     */
    public <R> LazyIter<R> map(Func<R, E> func) {
        return new LazyIter<R>(collection.map(func));
    }

    /**
     * Folds iterable collection left
     *
     * @param starting - initial value of the result
     * @param f        - folding function
     * @param <R>      - type of the resulting value
     * @return {@link Promise} of the value of the left-folded collection
     */
    public <R> Promise<R> reduce(final R starting, final Function2<R, R, E> f) {
        return new Promise<R>() {
            @Override
            public R get() {
                return collection.reduce(starting, f);
            }
        };
    }

    /**
     * Filters out elements of iterable collection based on predicate. Supplied matcher predicate should return
     * {@code true} if the element must be included into resulting collection or {@code false} otherwise.
     *
     * @param predicate - function to verify collection element
     * @return {@link Iterable} collection. This function will not create resulting collection immediately, instead next
     * element will be evaluated when requested with {@link java.util.Iterator#next()}
     */
    public LazyIter<E> filter(Predicate<E> predicate) {
        return new LazyIter<E>(collection.filter(predicate));
    }

    /**
     * This method is <strong>not lazy</strong>
     *
     * @return first element of the collection
     * @throws NoSuchElementException if collection is empty
     */
    public E head() {
        return collection.iterator().next();
    }

    /**
     * This method is <strong>not lazy</strong>
     *
     * @return All the elements of the collection except for the first
     * @throws NoSuchElementException if collection is empty
     */
    public LazyIter<E> tail() {
        Iterator<E> iter = collection.iterator();
        iter.next();
        return new LazyIter<E>(iter);
    }

    /**
     * Finds element in collection that matches supplied {@link Predicate}
     *
     * @param matcher      {@link Predicate} to test collection's elements
     * @param defaultValue default value that will be returned if none of the elements in the collection matches predicate
     * @return {@link Promise} of the element found or of the default value
     */
    public Promise<E> find(final Predicate<E> matcher, final E defaultValue) {
        return new Promise<E>() {
            @Override
            public E get() {
                return collection.find(matcher, defaultValue);
            }
        };
    }

    /**
     * Checks if all elements of collection match supplied predicate
     *
     * @param matcher - {@link Predicate} to check elements
     * @return {@link Promise} to return {@code true} if all elements match predicate or {@code false} otherwise
     */
    public Promise<Boolean> all(final Predicate<E> matcher) {
        return new Promise<Boolean>() {
            @Override
            public Boolean get() {
                return collection.all(matcher);
            }
        };
    }

    /**
     * Checks if any of the elements of collection match supplied predicate
     *
     * @param matcher - {@link Predicate} to check elements
     * @return {@link Promise} to return {@code true} if any of the elements match predicate or {@code false} otherwise
     */
    public Promise<Boolean> any(final Predicate<E> matcher) {
        return new Promise<Boolean>() {
            @Override
            public Boolean get() {
                return collection.any(matcher);
            }
        };
    }
}

