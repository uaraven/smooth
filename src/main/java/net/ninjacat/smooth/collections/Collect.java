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
    public static <E> List<E> listOf(final E... elements) {
        return Collections.unmodifiableList(Arrays.asList(elements));
    }

    /**
     * Create a set of elements
     *
     * @param elements elements of the future set
     * @param <E>      type of the elements
     * @return Unmodifiable set
     */
    public static <E> Set<E> setOf(final E... elements) {
        final Set<E> result = new HashSet<E>();
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
    public static <E> List<E> iteratorToList(final Iterator<E> iter) {
        return Collections.unmodifiableList((List<? extends E>) iteratorToCollection(iter, new ArrayList<E>()));
    }

    /**
     * Converts {@link java.util.Iterator} to a set
     *
     * @param iter iterator to read into a set
     * @param <E>  type of elements
     * @return Unmodifiable set of elements from iterator
     */
    public static <E> Set<E> iteratorToSet(final Iterator<E> iter) {
        return Collections.unmodifiableSet((Set<? extends E>) iteratorToCollection(iter, new HashSet<E>()));
    }

    /**
     * Wraps {@link Enumeration} in the {@link Iterator}.
     *
     * @param enumeration Enumeration to wrap.
     * @param <E>         Element type.
     * @return Iterator wrapping supplied enumeration.
     */
    public static <E> Iterator<E> enumerationToIterator(final Enumeration<E> enumeration) {
        return new Iterator<E>() {

            @Override
            public boolean hasNext() {
                return enumeration.hasMoreElements();
            }

            @Override
            public E next() {
                return enumeration.nextElement();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Remove operation is not supported");
            }
        };
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
    private static <E> Collection<E> iteratorToCollection(final Iterator<E> iter, final Collection<E> collection) {
        while (iter.hasNext()) {
            collection.add(iter.next());
        }
        return collection;
    }
}
