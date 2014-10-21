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

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class Iters {

    private Iters() {
    }

    /**
     * Generate iterable which returns integers in range [from; to)
     * @param from starting value
     * @param to upper bound
     * @return rich iterator {@link net.ninjacat.smooth.iterators.Iter}
     */
    public static Iter<Integer> range(final int from, final int to) {
        return Iter.of(new Iterator<Integer>() {
            private int position = from;

            @Override
            public boolean hasNext() {
                return position < to;
            }

            @Override
            public Integer next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                int result = position;
                position += 1;
                return result;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("ranges does not support remove()");
            }
        });
    }

    public static Iter<Integer> range(int to) {
        return range(0, to);
    }

    public static <T> Iter<T> multi(final T value, final int count) {
        return Iter.of(new Iterator<T>() {
            private int position = 0;

            @Override
            public boolean hasNext() {
                return position < count;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                position += 1;
                return value;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("ranges does not support remove()");
            }
        });
    }
}
