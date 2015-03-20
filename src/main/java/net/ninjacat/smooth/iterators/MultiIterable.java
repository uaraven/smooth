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

import java.util.*;

/**
 * Iterable that can host multiple {@link java.lang.Iterable}s and provide an {@link java.util.Iterator}
 * to walk them all.
 */
public class MultiIterable<T> implements Iterable<T> {
    private final List<Iterable<T>> collections;

    public MultiIterable(final Iterable<T>... iterables) {
        this.collections = new ArrayList<Iterable<T>>(Arrays.asList(iterables));
    }

    public MultiIterable() {
        this.collections = new ArrayList<Iterable<T>>();
    }

    public void append(final Iterable<T> iterable) {
        this.collections.add(iterable);
    }

    @Override
    public Iterator<T> iterator() {
        return new MultiIterator();
    }

    private class MultiIterator implements Iterator<T> {

        private final Iterator<Iterable<T>> masterIterator;
        private Iterator<T> slaveIterator;

        private boolean noMoreItems;

        private MultiIterator() {
            this.masterIterator = MultiIterable.this.collections.iterator();
            this.noMoreItems = !moveToNextCollection();
        }

        @Override
        public boolean hasNext() {
            if (this.noMoreItems) {
                return false;
            }
            if (this.slaveIterator.hasNext()) {
                return true;
            } else {
                if (moveToNextCollection()) {
                    return this.slaveIterator.hasNext();
                } else {
                    this.noMoreItems = true;
                    return false;
                }
            }
        }

        @Override
        public T next() {
            if (hasNext()) {
                return this.slaveIterator.next();
            }
            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private boolean moveToNextCollection() {
            if (this.masterIterator.hasNext()) {
                this.slaveIterator = this.masterIterator.next().iterator();
                return true;
            } else {
                return false;
            }
        }
    }
}
