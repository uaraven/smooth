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

import net.ninjacat.smooth.functions.Func;
import net.ninjacat.smooth.functions.Predicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Lists {

    private Lists() {
    }

    public static <E, R> List<R> map(final List<E> list, final Func<R, E> mapFunc) {
        final List<R> result = new ArrayList<R>(list.size());
        for (final E e : list) {
            result.add(mapFunc.apply(e));
        }
        return Collections.unmodifiableList(result);
    }

    public static <E> List<E> filter(final List<E> list, final Predicate<E> filterFunc) {
        final List<E> result = new ArrayList<E>();
        for (final E e : list) {
            if (filterFunc.matches(e)) {
                result.add(e);
            }
        }
        return Collections.unmodifiableList(result);
    }

    public static <T> List<T> of(final T... elements) {
        return Arrays.asList(elements);
    }
}
