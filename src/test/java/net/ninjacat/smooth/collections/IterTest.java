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

import net.ninjacat.smooth.functions.*;
import net.ninjacat.smooth.iterators.Iter;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static net.ninjacat.smooth.collections.IterFixtures.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class IterTest {

    @Test
    public void toListShouldReturnOriginalList() throws Exception {
        List<String> original = Arrays.asList("1", "1", "Last");
        List<String> result = Iter.of(original).toList();

        assertThat(result, is(original));
    }

    @Test
    public void toSetShouldReturnOnlyUniqueElements() throws Exception {
        List<String> original = Arrays.asList("1", "1", "Last");
        Set<String> result = Iter.of(original).toSet();

        assertThat(result, is(Collect.setOf("1", "Last")));
    }

    @Test
    public void mapShouldTransformValues() throws Exception {
        Iter<Integer> iter = Iter.of(Arrays.asList(1, 2, 3));
        Iterable<Integer> transformed = iter.map(new Func<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return integer * 2;
            }
        });
        Iterator<Integer> iterator = transformed.iterator();
        verifyNext(iterator, 2);
        verifyNext(iterator, 4);
        verifyNext(iterator, 6);
        verifyNoNext(iterator);
    }

    @Test
    public void mapShouldBeLazy() throws Exception {
        Iter<Integer> iter = Iter.of(Arrays.asList(1, 2, 3));

        final SideEffect sideEffect = new SideEffect();

        iter.map(new Func<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                sideEffect.sideEffect();
                return integer * 2;
            }
        });
        assertThat(sideEffect.hasSideEffects(), is(false));
    }

    @Test
    public void filterShouldReturnOddValues() throws Exception {
        Iter<Integer> iter = Iter.of(Arrays.asList(1, 2, 3));
        Iterable<Integer> filtered = iter.filter(new Predicate<Integer>() {
            @Override
            public boolean matches(Integer integer) {
                return integer % 2 != 0;
            }
        });
        Iterator<Integer> iterator = filtered.iterator();
        verifyNext(iterator, 1);
        verifyNext(iterator, 3);
        verifyNoNext(iterator);
    }

    @Test
    public void filterShouldBeLazy() throws Exception {
        Iter<Integer> iter = Iter.of(Arrays.asList(1, 2, 3));

        final SideEffect sideEffect = new SideEffect();

        iter.filter(new Predicate<Integer>() {
            @Override
            public boolean matches(Integer integer) {
                sideEffect.sideEffect();
                return integer % 2 != 0;
            }
        });

        assertThat(sideEffect.hasSideEffects(), is(false));
    }


    @Test
    public void reduceShouldFoldLeft() throws Exception {
        Iter<Integer> iter = Iter.of(Arrays.asList(1, 2, 3, 4));

        int result = iter.reduce(0, new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        });

        assertThat(result, is(10));
    }

    @Test
    public void lazyReduceShouldFoldLeft() throws Exception {
        Iter<Integer> iter = Iter.of(Arrays.asList(1, 2, 3, 4));

        Promise<Integer> result = iter.lazyReduce(0, new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        });

        assertThat(result.get(), is(10));
    }

    @Test
    public void lazyReduceShouldBeLazy() throws Exception {
        Iter<Integer> iter = Iter.of(Arrays.asList(1, 2, 3, 4));

        final SideEffect sideEffect = new SideEffect();

        iter.lazyReduce(0, new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) {
                sideEffect.sideEffect();
                return integer + integer2;
            }
        });

        assertThat(sideEffect.hasSideEffects(), is(false));
    }

    @Test
    public void forEachShouldIterateOverAllElements() throws Exception {
        Iter<Integer> iter = Iter.of(1, 2, 3, 4);

        final int[] sum = {0};
        iter.forEach(new Procedure<Integer>() {
            @Override
            public void call(Integer integer) {
                sum[0] = sum[0] + integer;
            }
        });

        assertThat(sum[0], is(10));
    }

    @Test
    public void findShouldBeAbleToLocateElement() throws Exception {
        Iter<Integer> iter = Iter.of(Arrays.asList(1, 2, 3, 4));

        Integer result = iter.find(new Predicate<Integer>() {
            @Override
            public boolean matches(Integer integer) {
                return integer.equals(3);
            }
        }, -1);

        assertThat(result, is(3));
    }

    @Test
    public void findShouldReturnDefaultValueIfElementNotFound() throws Exception {
        Iter<Integer> iter = Iter.of(Arrays.asList(1, 2, 3, 4));

        Integer result = iter.find(new Predicate<Integer>() {
            @Override
            public boolean matches(Integer integer) {
                return integer.equals(5);
            }
        }, -1);

        assertThat(result, is(-1));
    }

    @Test
    public void anyShouldReturnTrueIfAnyMatchingElementFound() throws Exception {
        Iter<String> iter = Iter.of("First", "Middle", "Last");

        boolean result = iter.any(new Predicate<String>() {
            @Override
            public boolean matches(String o) {
                return o.startsWith("M");
            }
        });

        assertThat(result, is(true));
    }

    @Test
    public void anyShouldReturnFalseIfNoMatchingElementFound() throws Exception {
        Iter<String> iter = Iter.of("First", "Middle", "Last");

        boolean result = iter.any(new Predicate<String>() {
            @Override
            public boolean matches(String o) {
                return o.startsWith("S");
            }
        });

        assertThat(result, is(false));
    }

    @Test
    public void allShouldReturnTrueIfAllElementsMatchPredicate() throws Exception {
        Iter<String> iter = Iter.of("Mary", "Molly", "Maria");

        boolean result = iter.all(new Predicate<String>() {
            @Override
            public boolean matches(String o) {
                return o.startsWith("M");
            }
        });

        assertThat(result, is(true));
    }

    @Test
    public void allShouldReturnFalseIfAtLeastOneElementDoesNotMatchPredicate() throws Exception {
        Iter<String> iter = Iter.of("Mary", "Molly", "Jenny");

        boolean result = iter.all(new Predicate<String>() {
            @Override
            public boolean matches(String o) {
                return o.startsWith("M");
            }
        });

        assertThat(result, is(false));
    }

}
