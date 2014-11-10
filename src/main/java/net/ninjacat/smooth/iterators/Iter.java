/*
 * Copyright 2014 Oleksiy Voronin <ovoronin@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.ninjacat.smooth.iterators;

import net.ninjacat.smooth.collections.Collect;
import net.ninjacat.smooth.functions.*;

import java.util.*;

/**
 * <p>Functional style immutable rich iterator</p>
 * <p/>
 * <p>Main difference from standard {@link java.util.Iterator} is that this iterator allows functions to be applied to the elements.
 * It supports standard operations like {@link #filter(net.ninjacat.smooth.functions.Predicate)}, {@link #map(net.ninjacat.smooth.functions.Func)}
 * or {@link #reduce(Object, net.ninjacat.smooth.functions.Function2)} and others.
 * </p>
 * <p/>
 * <p>This is essentially a rich wrapper around {@link Iterator} over collection. Standard limitations of iterators apply,
 * like restriction of changing collection during iteration</p>
 *
 * @param <E> Type of the elements in the iterator
 */
public class Iter<E> implements Iterable<E> {
    private final Iterator<E> iterator;

    Iter(Iterator<E> c) {
        iterator = c;
    }

    public static <E> Iter<E> of(Enumeration<E> enumeration) {
        return new Iter<E>(Collect.enumerationToIterator(enumeration));
    }

    /**
     * Creates iterator over collection. If the collection is changed during iteration (especially possible with lazy calculations)
     * {@link java.util.ConcurrentModificationException} may be thrown.
     *
     * @param coll collection to be wrapped
     * @param <E>  type of elements in the collection
     * @return Rich iterator over collection elements
     */
    public static <E> Iter<E> of(Collection<E> coll) {
        return new Iter<E>(coll.iterator());
    }

    /**
     * <p>
     * Creates rich iterator wrapper around Java {@link java.util.Iterator}.
     * </p><p>
     * Do not use wrapped iterator as it will interfere with operations over rich iterator.
     * </p>
     *
     * @param iter {@link java.util.Iterator} to wrap with rich functionality
     * @param <E>  type of elements in the collection
     * @return Rich iterator wrapped around Java iterator
     */
    public static <E> Iter<E> of(Iterator<E> iter) {
        return new Iter<E>(iter);
    }

    /**
     * <p>
     * Creates rich iterator wrapper around array of elements
     * </p>
     * <p>
     * This iterator is created over copy of the array, so original array can be used or disposed of.
     * </p>
     *
     * @param data the array
     * @param <E>  type of elements
     * @return Rich iterator for array elements
     */
    public static <E> Iter<E> of(E... data) {
        return Iter.of(Arrays.asList(data));
    }

    /**
     * @return {@link java.util.List} containing all the items from this iterator. Returned list is immutable
     */
    public List<E> toList() {
        return Collect.iteratorToList(iterator);
    }

    /**
     * @return {@link java.util.Set} containing all the items from this iterator. Returned list is immutable
     */
    public Set<E> toSet() {
        return Collect.iteratorToSet(iterator);
    }


    /**
     * <p>
     * Returns an array containing all of the elements in this iterable in proper sequence (from first to last element);
     * the runtime type of the returned array is that of the specified array. If the list fits in the specified array,
     * it is returned therein. Otherwise, a new array is allocated with the runtime type of the specified array and the
     * size of this Iterable.
     * </p><p>
     * If the list fits in the specified array with room to spare (i.e., the array has more elements than the iterable),
     * the element in the array immediately following the end of the list is set to null.
     * (This is useful in determining the length of the list only if the caller knows that the list does not contain any null elements.)
     * </p>
     *
     * @param array the array into which the elements of this list are to be stored, if it is big enough; otherwise,
     *              a new array of the same runtime type is allocated for this purpose.
     * @return an array containing the elements of this iterable
     */
    public E[] toArray(E[] array) {
        return toList().toArray(array);
    }

    /**
     * <p>Maps all the values in the iterator to other values using supplied function.</p>
     * <p>This function will not create new iterator, instead transformation function will be applied to
     * the next original iterator's element during each call to {@link java.util.Iterator#next()} </p>
     *
     * @param func mapping function
     * @param <R>  result type
     * @return Iterable iterator of mapped values
     */
    public <R> Iter<R> map(final Func<R, E> func) {
        return new Iter<R>(
                new Iterator<R>() {

                    @Override
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    @Override
                    public R next() {
                        return func.apply(iterator.next());
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException("Mapped rich iterators does not support remove()");
                    }
                });
    }

    /**
     * Calls specified {@link Procedure} for each element
     *
     * @param executor {@link Procedure} to be executed on each element of iterable
     */
    public void forEach(Procedure<E> executor) {
        while (iterator.hasNext()) {
            executor.call(iterator.next());
        }
    }

    /**
     * Folds iterable iterator left
     *
     * @param starting initial value of the result
     * @param f        folding function
     * @param <R>      type of the resulting value
     * @return Value of the left-folded collection
     */
    public <R> R reduce(R starting, Function2<R, R, E> f) {
        R result = starting;
        while (iterator.hasNext()) {
            result = f.apply(result, iterator.next());
        }
        return result;

    }

    /**
     * Lazily folds iterable iterator left
     *
     * @param starting initial value of the result
     * @param f        folding function
     * @param <R>      type of the resulting value
     * @return {@link Promise} of the value of the left-folded collection
     */
    public <R> Promise<R> lazyReduce(final R starting, final Function2<R, R, E> f) {
        return new Promise<R>() {
            @Override
            public R get() {
                R result = starting;
                while (iterator.hasNext()) {
                    result = f.apply(result, iterator.next());
                }
                return result;
            }
        };
    }

    /**
     * Filters out elements of iterable iterator based on predicate. Supplied matcher predicate should return
     * {@code true} if the element must be included into resulting iterator or {@code false} otherwise.
     *
     * @param predicate - function to verify iterator element
     * @return {@link Iterable} iterator. This function will not create resulting iterator immediately, instead next
     * element will be evaluated when requested with {@link java.util.Iterator#next()}
     */
    public Iter<E> filter(final Predicate<E> predicate) {
        return new Iter<E>(new Iterator<E>() {
            private E nextValue;

            public boolean hasNext() {
                if (nextValue != null) {
                    return true;
                }
                while (iterator.hasNext()) {
                    nextValue = iterator.next();
                    if (predicate.matches(nextValue)) {
                        return true;
                    }
                }
                nextValue = null;
                return false;
            }

            @Override
            public E next() {
                if (nextValue != null) {
                    E result = nextValue;
                    nextValue = null;
                    return result;
                }
                throw new NoSuchElementException();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        });
    }

    /**
     * Finds first element in iterator that matches supplied {@link Predicate}
     *
     * @param matcher      {@link Predicate} to test iterator's elements
     * @param defaultValue default value that will be returned if none of the elements in the iterator matches predicate
     * @return Element found or of the default value
     */
    public E find(Predicate<E> matcher, E defaultValue) {
        while (iterator.hasNext()) {
            E next = iterator.next();
            if (matcher.matches(next)) {
                return next;
            }
        }
        return defaultValue;
    }

    /**
     * Lazy variant of {@link #find(net.ninjacat.smooth.functions.Predicate, Object)}
     *
     * @param matcher      {@link net.ninjacat.smooth.functions.Predicate} to test iterator elements
     * @param defaultValue default value that will be returned if none of the elements in the iterator matches predicate
     * @return Promise to find element that matches predicate
     */
    public Promise<E> lazyFind(final Predicate<E> matcher, final E defaultValue) {
        return new Promise<E>() {
            @Override
            public E get() {
                while (iterator.hasNext()) {
                    E next = iterator.next();
                    if (matcher.matches(next)) {
                        return next;
                    }
                }
                return defaultValue;
            }
        };
    }


    /**
     * Checks if all elements of iterator match supplied predicate
     *
     * @param matcher - {@link Predicate} to check elements
     * @return {@code true} if all elements match predicate or {@code false} otherwise
     */
    public boolean all(Predicate<E> matcher) {
        while (iterator.hasNext()) {
            if (!matcher.matches(iterator.next())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if any of the elements of iterator match supplied predicate
     *
     * @param matcher - {@link Predicate} to check elements
     * @return {@code true} if any of the elements match predicate or {@code false} otherwise
     */
    public boolean any(Predicate<E> matcher) {
        while (iterator.hasNext()) {
            if (matcher.matches(iterator.next())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return iterator;
    }

    public String mkStr(String separator) {
        StringBuilder builder = new StringBuilder();
        while (iterator.hasNext()) {
            builder.append(iterator.next().toString()).append(separator);
        }
        builder.setLength(builder.length() - separator.length());
        return builder.toString();
    }

}

